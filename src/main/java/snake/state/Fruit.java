package snake.state;

import java.util.Random;

import snake.common.Point;
import snake.protocol.state.StateUpdate;

public class Fruit implements GameObject {
    private final Point position;

    public Fruit(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void build(Board board) {
        board.getCell(position).getStack().push(this);
    }

    @Override
    public void step() {
    }
}
