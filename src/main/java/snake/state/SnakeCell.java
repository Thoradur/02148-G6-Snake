package snake.state;


public class SnakeCell extends CellEntity {
    private final Snake snake;

    public SnakeCell(Snake snake, Cell parent) {
        super(parent);
        this.snake = snake;
    }

    public boolean isHead() {
        return this.equals(snake.getHead());
    }

    public boolean isTail() {
        return this.equals(snake.getTail());
    }

    @Override
    public void destroy() {
        super.destroy();
        this.snake.removeCell(this);
    }

    @Override
    public String toString() {
        return String.format("SnakeCell(%d, %d%s)", getX(), getY(), isHead() ? ", H" : (isTail() ? ", T" : ""));
    }
}
