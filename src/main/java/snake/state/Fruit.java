package snake.state;

import java.util.Random;

import snake.common.Point;
import snake.protocol.state.Fragment;

public class Fruit implements GameObject {

    Point position;

    public Fruit(Point position) {
        this.position = position;
    }

    public Fruit(Board board, int seed) {
        positionFruit(board, seed);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
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
                setPosition(position);
                break;
            }
        }
    }

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public Fragment step() {
        return null;
    }
}
