package snake;

import snake.state.BoardEntity;
import snake.state.Cell;
import snake.state.CellEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Snake implements BoardEntity {

    private final LinkedList<SnakeCell> snake;

    public Snake(LinkedList<SnakeCell> snake) {
        this.snake = snake;
    }

    public Snake(Cell startPosition) {
        this.snake = new LinkedList<>();
        this.snake.add(new SnakeCell(this, startPosition));
    }

    public static Snake createSnakeFromCompactSnake(Cell[] compactSnake) {
        var snake = new Snake(compactSnake[0]);

        for (var cell : compactSnake) {
            snake.snake.addLast(new SnakeCell(snake, cell));
        }

        return snake;
    }

    /**
     * Convert the linked list into the minimal amount of cells needed to represent the snake.
     * That being the head, the tail and every turning point.
     * <p>
     * Snake:
     * <br>
     * [(0,2), (1, 2), (2,2), (2, 1), (1, 1), (1, 0), (0, 0)]
     * <br>
     * Becomes:
     * <br>
     * [(0,2), (2,2), (2, 1), (1, 1), (1, 0), (0, 0)]
     *
     * @return Cell[]
     */
    public SnakeCell[] getCompactSnake() {
        List<SnakeCell> compactSnake = new ArrayList<>();

        compactSnake.add(snake.getFirst());

        for (var cell : snake) {
            var lastCell = compactSnake.get(compactSnake.size() - 1);

            if (lastCell.getX() != cell.getX() && lastCell.getY() != cell.getX()) {
                compactSnake.add(cell);
            }
        }

        compactSnake.add(snake.getLast());

        return compactSnake.toArray(new SnakeCell[0]);
    }

    public void move(Cell nextCell) {
        snake.addFirst(new SnakeCell(this, nextCell));
        snake.removeLast();
    }

    /**
     * Growing stacks the given amount of cells on top of the last cell.
     *
     * @param size
     */
    public void grow(int size) {
        for (int i = 0; i < size; i++) {
            snake.addLast(new SnakeCell(this, snake.getLast().getParent()));
        }
    }

    @Override
    public void destroy() {
        snake.forEach(SnakeCell::destroy);
    }

    @Override
    public Iterable<CellEntity> getCells() {
        return Collections.unmodifiableList(snake);
    }
}
