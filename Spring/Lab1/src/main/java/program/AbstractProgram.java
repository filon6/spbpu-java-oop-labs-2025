package program;

import java.util.Random;

public class AbstractProgram implements Runnable {

    public enum ProgramState {
        UNKNOWN,
        STOPPING,
        RUNNING,
        FATAL_ERROR
    }

    private final Object lock = new Object();

    private ProgramState state = ProgramState.UNKNOWN;
    private long revision = 0;          // увеличивается на КАЖДОЕ изменение state
    private boolean terminated = false; // окончательное завершение

    private final int minDelayMs;
    private final int maxDelayMs;
    private final Random rnd = new Random();

    private Thread worker;
    private Thread daemon;

    public AbstractProgram() {
        this(300, 1200); // дефолтный интервал, чтобы final поля были инициализированы
    }

    public AbstractProgram(int minDelayMs, int maxDelayMs) {
        if (minDelayMs <= 0 || maxDelayMs < minDelayMs) {
            throw new IllegalArgumentException("Bad delay interval");
        }
        this.minDelayMs = minDelayMs;
        this.maxDelayMs = maxDelayMs;
    }

    // =======================
    // API ДЛЯ СУПЕРВИЗОРА
    // =======================

    /** Переводит программу в RUNNING и гарантирует запуск потоков (один раз). */
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

    /** Переводит программу в STOPPING. */
    public void stopProgram() {
        synchronized (lock) {
            if (terminated) return;
            setStateLocked(ProgramState.STOPPING, "Supervisor");
        }
    }

    /** Окончательно завершает программу (после этого worker и daemon должны выйти). */
    public void terminateProgram() {
        synchronized (lock) {
            if (terminated) return;
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

    /**
     * Ждёт, пока revision станет больше lastSeenRevision.
     * Это ключ к требованию "супервизор не пропускает ни одного статуса".
     */
    public Snapshot waitForNextChange(long lastSeenRevision) throws InterruptedException {
        synchronized (lock) {
            while (!terminated && revision <= lastSeenRevision) {
                lock.wait();
            }
            return new Snapshot(state, revision, terminated);
        }
    }

    public static final class Snapshot {
        public final ProgramState state;
        public final long revision;
        public final boolean terminated;

        public Snapshot(ProgramState state, long revision, boolean terminated) {
            this.state = state;
            this.revision = revision;
            this.terminated = terminated;
        }
    }

    // =======================
    // ЛОГИКА "ПРОГРАММЫ"
    // =======================

    @Override
    public void run() {
        System.out.println("[Program] Worker started");

        while (true) {
            synchronized (lock) {
                if (terminated) {
                    System.out.println("[Program] Worker exiting");
                    return;
                }
                // По ТЗ "абстрактная программа" может просто жить.
                // Реальную "работу" можно имитировать сном ниже.
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Если прервали, корректно завершаемся
                terminateProgram();
            }
        }
    }

    // =======================
    // ДЕМОН СЛУЧАЙНЫХ СОСТОЯНИЙ
    // =======================

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
                if (terminated) return;
                setStateLocked(next, "Daemon");
            }
        }
    }

    private ProgramState randomNonUnknownState() {
        // 0..2 -> RUNNING, STOPPING, FATAL_ERROR
        int x = rnd.nextInt(3);
        if (x == 0) return ProgramState.RUNNING;
        if (x == 1) return ProgramState.STOPPING;
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
        if (oldState == newState) return;

        this.state = newState;
        revision++;

        System.out.println("[" + who + "] State changed: " + oldState + " -> " + newState + " (rev=" + revision + ")");

        lock.notifyAll();
    }
}
