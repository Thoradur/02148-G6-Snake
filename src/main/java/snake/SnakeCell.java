package snake;


import snake.state.Cell;
import snake.state.CellEntity;

public class SnakeCell extends CellEntity {
    private final Snake snake;

    public SnakeCell(Snake snake, Cell parent) {
        super(parent);
        this.snake = snake;
    }
}
