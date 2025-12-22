package strategy;

import utils.Point;

public class FlyStrategy implements MoveStrategy {
    @Override
    public void move(Point from, Point to) {
        double distance = from.distance(to);
        System.out.printf("Пролетел %d м\n", (int) distance);
    }
}
