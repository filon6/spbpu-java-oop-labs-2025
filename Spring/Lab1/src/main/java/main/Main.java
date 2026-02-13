package main;

import program.AbstractProgram;
import supervisor.Supervisor;

public class Main {
    public static void main(String[] args) {
        try {
            AbstractProgram p = new AbstractProgram(300, 900);
            Supervisor s = new Supervisor(p);

            s.startSupervisor();

            Thread.sleep(7000);

            s.stopSupervisor();
        } catch (InterruptedException | IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
