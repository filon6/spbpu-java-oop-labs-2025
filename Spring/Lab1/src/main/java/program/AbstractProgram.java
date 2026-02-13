package program;

import java.util.Random;

public class AbstractProgram implements Runnable {
    private final Object lock = new Object();

    private ProgramState state;
    private int revision; // Счётчик изменений состояния
    private boolean terminated;

    private final int minDelayMs; // Интервал задержки демона между сменами состояния
    private final int maxDelayMs; // Delay - задержка
    private final Random rnd = new Random();

    private Thread worker;
    private Thread daemon;

    public AbstractProgram(int minDelayMs, int maxDelayMs) {
        if (minDelayMs <= 0 || maxDelayMs < minDelayMs) {
            throw new IllegalArgumentException("Bad delay interval");
        }
        this.minDelayMs = minDelayMs;
        this.maxDelayMs = maxDelayMs;
        this.state = ProgramState.UNKNOWN;
    }

    public void startProgram() {
        synchronized (lock) {
            if (terminated) return;

            if (worker == null) {
                worker = new Thread(this, "AbstractProgram-Worker");
                worker.start();
            }
            if (daemon == null) {
                daemon = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stateChangeDaemon();
                    }
                }, "AbstractProgram-Daemon");
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

    public ProgramState getState() {
        synchronized (lock) {
            return state;
        }
    }

    public long getRevision() {
        synchronized (lock) {
            return revision;
        }
    }

    public boolean isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    public long waitForNextRevision(long lastSeenRevision) throws InterruptedException {
        synchronized (lock) {
            while (!terminated && revision <= lastSeenRevision) {
                lock.wait();
            }
            return revision;
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

            ProgramState next = randomNonUnknownState();

            synchronized (lock) {
                if (terminated) {
                    return;
                }
                setStateLocked(next, "Daemon");
            }
        }
    }

    private ProgramState randomNonUnknownState() {
        int x = rnd.nextInt(3);
        if (x == 0) {
            return ProgramState.RUNNING;
        }
        if (x == 1) {
            return ProgramState.STOPPING;
        }
        return ProgramState.FATAL_ERROR;
    }

    private void sleepRandomInterval() {
        int delay = minDelayMs + rnd.nextInt(maxDelayMs - minDelayMs + 1);
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
