package strategy;

import utils.Point;

public class HorseStrategy implements MoveStrategy {
    @Override
    public void move(Point from, Point to) {
        double distance = from.distance(to);
        System.out.printf("Проехал на лошади %d м\n", (int) distance);
    }
}
