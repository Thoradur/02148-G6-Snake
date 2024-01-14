package snake.state;

import java.util.Random;

import snake.common.Point;
import snake.protocol.state.StateUpdate;

public abstract class Fruit implements GameObject {
    private final Point position;
    private final int points;
    private boolean eaten = false;

    public Fruit(Point position, int points) {
        this.position = position;
        this.points = points;
    }

    public Point getPosition() {
        return position;
    }

    public void eat() {
        eaten = true;
    }

    public boolean isEaten() {
        return eaten;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void build(Board board) {
        board.getCell(position).getStack().push(this);
    }

    @Override
    public void step() {
    }
}
