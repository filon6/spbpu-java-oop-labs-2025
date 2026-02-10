package characters;

import strategy.MoveStrategy;
import utils.Point;

public class Hero {
    private final String name;
    private Point position;
    private MoveStrategy strategy;

    public Hero(String name, Point position) {
        this.name = name;
        this.position = position;
    }

    public void setStrategy(MoveStrategy strategy) {
        this.strategy = strategy;
    }

    public void move(Point to) {
        strategy.move(position, to);
        this.position = to;
    }

    public Point getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
