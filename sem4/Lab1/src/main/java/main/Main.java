package main;

import program.AbstractProgram;
import supervisor.Supervisor;

public class Main {
    public static void main(String[] args) {
        try {
            AbstractProgram program = new AbstractProgram(300, 900);
            Supervisor supervisor = new Supervisor(program);

            supervisor.startSupervisor();

            Thread.sleep(7000);

            supervisor.stopSupervisor();
        } catch (InterruptedException | IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
