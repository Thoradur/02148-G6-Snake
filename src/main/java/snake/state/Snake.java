package snake.state;

import snake.common.Direction;
import snake.common.Point;
import snake.protocol.state.Fragment;

import java.util.*;
import java.util.stream.IntStream;

public class Snake implements GameObject {
    private int step = 0;
    private final LinkedList<Point> snake = new LinkedList<>();
    private Direction direction;

    public Snake(Point startPosition, Direction startDirection) {
        snake.add(startPosition);
        direction = startDirection;
    }

    /**
     * Instantiate a snake based on a minimal amount of information.
     */
    public Snake(List<Point> dehydratedSnake) {
        setDehydratedSnake(dehydratedSnake);
    }

    public int size() {
        return snake.size();
    }

    public int length() {
        HashSet<Point> uniquePoints = new HashSet<>(snake);
        return uniquePoints.size();
    }

    @Override
    public int getStep() {
        return step;
    }

    public Fragment step() {
        step++;
        snake.addFirst(getHead().add(this.direction));
        snake.removeLast();
        return new Fragment(step, getDehydratedSnake().toArray(new Point[0]), true);
    }

    public void grow(int size) {
        boolean isGrowing = size > 0;

        IntStream.range(0, Math.abs(size)).forEach(i -> {
            // Either pop or append depending on if size is negative or positive
            if (!isGrowing) {
                snake.removeLast();
                return;
            }

            // Make tail stack when growing
            snake.addLast(getTail());
        });
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Point getHead() {
        return snake.getFirst();
    }

    public Point getTail() {
        return snake.getLast();
    }

    public LinkedList<Point> getSnake() {
        return snake;
    }

    public List<Point> getDehydratedSnake() {
        List<Point> dehydratedSnake = new ArrayList<>();
        Direction direction = this.direction;

        dehydratedSnake.add(getHead());

        for (var point : snake) {
            var prevPoint = dehydratedSnake.getLast();
            var newDirection = Direction.fromPoints(prevPoint, point);

            if (newDirection != direction) {
                dehydratedSnake.add(point);
                direction = newDirection;
                continue;
            }

            if (getTail().equals(point)) {
                dehydratedSnake.add(point);
            }
        }

        return dehydratedSnake;
    }

    public void setDehydratedSnake(List<Point> dehydratedSnake) {
        snake.clear();

        if (dehydratedSnake.isEmpty()) return;

        var tail = dehydratedSnake.getLast();

        for (var anchorPoint : dehydratedSnake) {
            if (snake.isEmpty()) {
                snake.addLast(anchorPoint);
                continue;
            }

            var prevPoint = snake.getLast();

            direction = Direction.fromPoints(anchorPoint, prevPoint);

            if (prevPoint.equals(anchorPoint) && !anchorPoint.equals(tail)) {
                throw new IllegalArgumentException("Snake is malformed: " + dehydratedSnake);
            }

            if (prevPoint.distanceTo(anchorPoint) < 2) {
                snake.addLast(anchorPoint);
                continue;
            }

            try {
                anchorPoint.interpolateTo(prevPoint).toList().reversed().forEach(snake::addLast);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Snake is malformed at: " + anchorPoint + ", " + prevPoint);
            }
        }
    }

    @Override
    public void build(Board board) {


        for (var point : snake) {
            var cell = board.getCell(point);

            cell.getStack().push(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringSnake = new StringBuilder();

        for (var point : this.snake) {
            stringSnake.append(point.toString());

            if (point == this.snake.getLast()) continue;

            stringSnake.append(" -> ");
        }

        stringSnake.append('\n');

        return stringSnake.toString();
    }
}
