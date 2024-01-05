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
        Point prevPoint = compactSnake.removeFirst();

        var snake = new Snake(board.getCell(prevPoint.getX(), prevPoint.getY()));

        while (!compactSnake.isEmpty()) {
            var point = compactSnake.removeFirst();

            if (point.equals(prevPoint) && !compactSnake.isEmpty()) {
                throw new RuntimeException("Invalid compact snake, duplicate point not in tail");
            }

            var prevX = prevPoint.getX();
            var prevY = prevPoint.getY();

            var x = point.getX();
            var y = point.getY();

            if (prevX != x && prevY != y) {
                throw new RuntimeException("Invalid compact snake, diagonal movement");
            }

            int dx = x - prevX;
            int dy = y - prevY;

            if (dx < 0 || dy < 0) {
                throw new RuntimeException("Invalid compact snake, movement is not forward");
            }

            for (int i = 0; i < dx; i++) {
                snake.move(board.getCell(prevX + i, prevY));
            }

            for (int i = 0; i < dy; i++) {
                snake.move(board.getCell(x, prevY + i));
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

        compactSnake.add(new PointRef(snake.getFirst()));

        Direction direction = null;
        SnakeCell prevCell = null;

        var iterator = snake.iterator();

        for (var cell : snake) {
            Direction newDirection = Direction.betweenPoints(prevCell, cell);


            prevCell = cell;
        }


        for (var cell : snake) {
            Direction newDirection = Direction.betweenPoints(compactSnake.getLast(), cell);

            System.out.println("Direction: " + newDirection + " at point " + cell);

            if (cell.isTail() || !cell.isHead() && direction != null && !direction.equals(newDirection)) {
                compactSnake.add(new PointRef(cell));
            }

            direction = newDirection;

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

    // Visible lenght (Unique cells)
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
