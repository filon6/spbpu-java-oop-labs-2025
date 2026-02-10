package program;

public class Supervisor extends Thread {

    private final AbstractProgram program;
    private volatile boolean running = false;

    public Supervisor(AbstractProgram program) {
        super("Supervisor");
        this.program = program;
    }

    /** Запускает супервизор и стартует программу. */
    public synchronized void startSupervisor() {
        if (running) return;

        System.out.println("[Supervisor] Starting supervisor...");
        running = true;

        program.startProgram();

        // запускаем поток супервизора (этот класс extends Thread)
        this.start();
    }

    /** Останавливает супервизор (и переводит программу в STOPPING). */
    public synchronized void stopSupervisor() {
        if (!running) return;

        System.out.println("[Supervisor] Stop request...");
        running = false;

        // мягко остановим программу (по ТЗ у супервизора есть stop/start)
        program.stopProgram();

        // будим супервизор, если он ждёт изменения состояния
        this.interrupt();
    }

    @Override
    public void run() {
        System.out.println("[Supervisor] Supervisor thread started");

        long lastRev = program.getRevision();

        while (running) {
            try {
                // Ключ: ждём СЛЕДУЮЩЕЕ изменение, а не опрашиваем
                AbstractProgram.Snapshot snap = program.waitForNextChange(lastRev);
                lastRev = snap.revision;

                if (snap.terminated) {
                    System.out.println("[Supervisor] Program already terminated, stopping supervisor");
                    running = false;
                    break;
                }

                System.out.println("[Supervisor] Observed state: " + snap.state + " (rev=" + snap.revision + ")");

                if (snap.state == AbstractProgram.ProgramState.STOPPING) {
                    System.out.println("[Supervisor] STOPPING detected -> restarting program");
                    program.startProgram();
                } else if (snap.state == AbstractProgram.ProgramState.FATAL_ERROR) {
                    System.out.println("[Supervisor] FATAL_ERROR detected -> terminating program");
                    program.terminateProgram();
                    running = false;
                    break;
                }

            } catch (InterruptedException e) {
                // stopSupervisor() делает interrupt, это нормальный выход
                if (!running) break;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("[Supervisor] Error: " + e.getMessage());
                // на усмотрение: можно продолжать или падать
            }
        }

        System.out.println("[Supervisor] Supervisor thread terminated");
    }
}
