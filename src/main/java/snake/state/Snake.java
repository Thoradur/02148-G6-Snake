package snake.state;

import java.util.*;
import java.util.stream.IntStream;

public class Snake implements BoardEntity {

    private final LinkedList<SnakeCell> snake;

    public Snake(LinkedList<SnakeCell> snake) {
        this.snake = snake;
    }

    public Snake(Cell startPosition) {
        this.snake = new LinkedList<>();
        this.snake.add(new SnakeCell(this, startPosition));
    }

    public SnakeCell getHead() {
        return snake.getFirst();
    }

    public SnakeCell getTail() {
        return snake.getLast();
    }

    public static Snake createSnakeFromCompactSnake(Board board, List<Point> compactSnake) {
        Snake snake = new Snake(new LinkedList<>());

        Direction direction = null;
        Point prevPoint = compactSnake.getFirst();

        for (var point : compactSnake) {
            for (var int_point : PointRef.interpolate(prevPoint, point).reversed()) {
                var cell = board.getCell(int_point);
                snake.appendTail(cell);
            }

            prevPoint = point;
        }

        return snake;
    }

    /**
     * Convert the linked list into the minimal amount of cells needed to represent the snake.
     * That being the head, the tail and every turning point.
     * Snake: [(0,2), (1, 2), (2,2), (2, 1), (1, 1), (1, 0), (0, 0)]
     * Becomes: [(0,2), (2,2), (2, 1), (1, 1), (1, 0), (0, 0)]
     *
     * @return Cell[]
     */
    public List<Point> getCompactSnake() {
        List<Point> compactSnake = new ArrayList<>();

        Direction direction = null;
        SnakeCell prevCell = snake.getFirst();

        for (var cell : snake) {
            var newDirection = Direction.betweenPoints(prevCell, cell);

            if (cell.isHead() || cell.isTail()) {
                // Always include every head and tail
                compactSnake.add(new PointRef(cell));

            } else if (direction != newDirection) {
                // Only include turning points
                compactSnake.add(new PointRef(prevCell));
            }

            direction = newDirection;
            prevCell = cell;
        }

        return compactSnake;
    }

    public void removeCell(SnakeCell cell) {
        snake.remove(cell);
    }

    private void removeTail() {
        snake.removeLast().destroy();
    }

    private void appendTail(Cell cell) {
        snake.addLast(new SnakeCell(this, cell));
    }

    public void move(Cell nextCell) {
        // TODO add check to ensure dist is equal to 1
        snake.addFirst(new SnakeCell(this, nextCell));
        removeTail();
    }

    /**
     * Growing stacks the given amount of cells on top of the last cell.
     */
    public void grow(int size) {
        // Either pop or append depending on if size is negative or positive
        IntStream.range(0, Math.abs(size)).forEach(i -> {
            if (size < 0) {
                removeTail();
                return;
            }

            appendTail(snake.getLast().getParent());
        });
    }

    // Visible length (Unique cells)
    public int length() {
        HashSet<Integer> seenIndices = new HashSet<>();

        for (var cell : snake) {
            seenIndices.add(cell.getParent().hashCode());
        }

        return seenIndices.size();
    }

    public int size() {
        return snake.size();
    }

    @Override
    public void destroy() {
        while (!snake.isEmpty()) {
            snake.removeLast().destroy();
        }
    }

    @Override
    public Iterable<CellEntity> getCells() {
        return Collections.unmodifiableList(snake);
    }
}
