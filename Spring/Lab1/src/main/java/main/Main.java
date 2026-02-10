package main;

import program.AbstractProgram;
import program.Supervisor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AbstractProgram p = new AbstractProgram(300, 900);
        Supervisor s = new Supervisor(p);

        s.startSupervisor();

        Thread.sleep(7000);

        s.stopSupervisor();
    }
}
