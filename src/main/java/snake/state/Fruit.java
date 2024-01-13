package snake.state;

import java.util.Random;

import snake.common.Point;
import snake.protocol.state.StateUpdate;

public class Fruit implements GameObject {
    private Point position;

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

    public void positionFruit(Board board, int seed) {
        Random random = new Random(seed);
        while (true) {
            int x = random.nextInt(board.getWidth());
            int y = random.nextInt(board.getHeight());
            Point position = new Point(x, y);
            if (board.getCell(position).getStack().isEmpty()) {
                break;
            }
        }
    }

    @Override
    public void step() {
    }
}
