package supervisor;

import program.AbstractProgram;
import program.ProgramState;

public class Supervisor extends Thread {
    private final AbstractProgram program;
    private volatile boolean running;

    public Supervisor(AbstractProgram program) {
        super("Supervisor");
        this.program = program;
    }

    public synchronized void startSupervisor() {
        if (running) {
            return;
        }

        System.out.println("[Supervisor] Starting supervisor...");
        running = true;

        program.startProgram();
        this.start();
    }

    public synchronized void stopSupervisor() {
        if (!running) {
            return;
        }

        System.out.println("[Supervisor] Stop request...");
        running = false;

        program.stopProgram();
        this.interrupt();
    }

    @Override
    public void run() {
        System.out.println("[Supervisor] Supervisor thread started");

        int lastRev = program.getRevision();

        while (running) {
            try {
                lastRev = program.waitForNextRevision(lastRev);

                if (program.isTerminated()) {
                    System.out.println("[Supervisor] Program terminated -> stopping supervisor");
                    break;
                }

                ProgramState state = program.getState();
                System.out.println("[Supervisor] Observed state: " + state + " (rev=" + lastRev + ")");

                if (state == ProgramState.STOPPING) {
                    System.out.println("[Supervisor] STOPPING detected -> restarting program");
                    program.startProgram();
                } else if (state == ProgramState.FATAL_ERROR) {
                    System.out.println("[Supervisor] FATAL_ERROR detected -> terminating program");
                    program.terminateProgram();
                    break;
                }

            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("[Supervisor] Supervisor thread terminated");
    }
}
