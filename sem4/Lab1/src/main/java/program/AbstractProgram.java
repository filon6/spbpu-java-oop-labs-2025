package program;

import java.util.Random;

import static program.ProgramState.*;

public class AbstractProgram implements Runnable {
    private final Object lock = new Object();

    private ProgramState state = ProgramState.UNKNOWN;
    private int revision;
    private boolean terminated;
    private final int minDelayMs;
    private final int maxDelayMs;
    private final Random random = new Random();

    private Thread worker;
    private Thread daemon;

    public AbstractProgram(int minDelayMs, int maxDelayMs) {
        if (minDelayMs <= 0 || maxDelayMs < minDelayMs) {
            throw new IllegalArgumentException("Bad delay interval");
        }
        this.minDelayMs = minDelayMs;
        this.maxDelayMs = maxDelayMs;
    }

    public void startProgram() {
        synchronized (lock) {
            if (terminated) {
                return;
            }
            if (worker == null) {
                worker = new Thread(this, "AbstractProgram-Worker");
                worker.start();
            }
            if (daemon == null) {
                daemon = new Thread(this::stateChangeDaemon, "AbstractProgram-Daemon");
                daemon.setDaemon(true);
                daemon.start();
            }

            setStateLocked(ProgramState.RUNNING, "Supervisor");
        }
    }

    public void stopProgram() {
        synchronized (lock) {
            if (terminated) {
                return;
            }
            setStateLocked(ProgramState.STOPPING, "Supervisor");
        }
    }

    public void terminateProgram() {
        synchronized (lock) {
            if (terminated) {
                return;
            }
            terminated = true;
            revision++;
            System.out.println("[Supervisor] Program terminated (rev=" + revision + ")");
            lock.notifyAll();
        }
    }

    public int waitForNextRevision(int lastSeenRevision) throws InterruptedException {
        synchronized (lock) {
            while (!terminated && revision <= lastSeenRevision) {
                lock.wait();
            }
            return revision;
        }
    }

    public ProgramState getState() {
        synchronized (lock) {
            return state;
        }
    }

    public int getRevision() {
        synchronized (lock) {
            return revision;
        }
    }

    public boolean isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    @Override
    public void run() {
        System.out.println("[Program] Worker started");

        while (true) {
            synchronized (lock) {
                if (terminated) {
                    System.out.println("[Program] Worker exiting");
                    return;
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                terminateProgram();
            }
        }
    }

    private void stateChangeDaemon() {
        System.out.println("[Daemon] Started");

        while (true) {
            synchronized (lock) {
                if (terminated) {
                    System.out.println("[Daemon] Exiting (program terminated)");
                    return;
                }
            }

            sleepRandomInterval();

            ProgramState nextState = randomNonUnknownState();

            synchronized (lock) {
                if (terminated) {
                    return;
                }
                setStateLocked(nextState, "Daemon");
            }
        }
    }

    private ProgramState randomNonUnknownState() {
        ProgramState[] values = {RUNNING, STOPPING, FATAL_ERROR};
        return values[random.nextInt(values.length)];
    }

    private void sleepRandomInterval() {
        int delay = minDelayMs + random.nextInt(maxDelayMs - minDelayMs + 1);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void setStateLocked(ProgramState newState, String who) {
        ProgramState oldState = this.state;
        if (oldState == newState) {
            return;
        }

        this.state = newState;
        revision++;

        System.out.println("[" + who + "] State changed: " + oldState + " -> " + newState + " (rev=" + revision + ")");
        lock.notifyAll();
    }
}
