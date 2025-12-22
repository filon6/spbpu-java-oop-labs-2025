package strategy;

import utils.Point;

public class WalkStrategy implements MoveStrategy{
    @Override
    public void move(Point from, Point to) {
        double distance = from.distance(to);
        System.out.printf("Прошел %d м\n", (int) distance);
    }
}
