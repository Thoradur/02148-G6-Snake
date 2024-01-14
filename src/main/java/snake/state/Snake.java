package snake.state;

import snake.common.Direction;
import snake.common.Point;
import snake.protocol.state.StateUpdate;

import java.util.*;
import java.util.stream.IntStream;

import javafx.application.Platform;

public class Snake implements GameObject {
    boolean isDead = false;
    private final LinkedList<Point> snake = new LinkedList<>();
    private Direction direction;

    public Snake(Point startPosition, Direction startDirection) {
        snake.add(startPosition);
        direction = startDirection;
    }

    /**
     * Instantiate a snake based on a minimal amount of information.
     */
    public boolean isDead() {
        return isDead;
    }

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

    public void step() {
        System.out.println("Snake is stepping in direction: " + this.direction);
        snake.addFirst(getHead().add(this.direction));
        snake.removeLast();
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

    public void kill() {
        isDead = true;
    }

    public void collision(Board board) {
        Cell headCell = board.getCell(getHead());

        // Check for collision with other snakes
        for (var gameObject : headCell.getStack()) {
            if (gameObject instanceof Snake otherSnake) {
                if (otherSnake != this) {
                    System.out.println("Snake has collided with another snake");
                    kill();
                    // Handle collision with another snake
                    return;
                }
            }
        }

        // Check for collision with itself
        for (var point : snake) {
            if (point != getHead() && point.equals(getHead())) {
                System.out.println("Snake has collided with itself");
                isDead = true;
                // Handle self-collision
                return;
            }
        }

        // Check for collision with fruit
        for (var gameObject : headCell.getStack()) {
            // Handle collision with fruit
            if (gameObject instanceof Fruit f) {
                grow(1);
                board.getState().getGameObjects().remove(f);
            }
        }
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

        Direction localDirection = null;

        for (var i = 0; i < snake.size(); i++) {
            var point = snake.get(i);

            if (dehydratedSnake.isEmpty()) {
                dehydratedSnake.add(point);
                continue;
            }

            var prevPoint = snake.get(i - 1);
            var nextDirection = Direction.fromPoints(prevPoint, point);

            if (localDirection == null) {
                localDirection = nextDirection;
                continue;
            }

            if (localDirection != nextDirection) {
                dehydratedSnake.add(prevPoint);
                localDirection = nextDirection;
            }
        }

        // Add all tail points
        var tailPoint = getTail();

        // Count tail points in self by iterating backwards and stopping at the first
        // non-tail point (use stream)
        var tailPoints = (int) IntStream.range(0, snake.size()).mapToObj(i -> snake.get(snake.size() - i - 1)).takeWhile(tailPoint::equals).count();
        var tailPointsOther = (int) IntStream.range(0, dehydratedSnake.size()).mapToObj(i -> dehydratedSnake.get(dehydratedSnake.size() - i - 1)).takeWhile(tailPoint::equals).count();

        // Add missing tail points
        IntStream.range(0, tailPoints - tailPointsOther).forEach(i -> dehydratedSnake.add(tailPoint));

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
            if (cell == null) {
                System.out.println("Snake is out of bounds: " + point);
                Platform.exit();
                System.exit(0);
            }

            if (getTail().equals(point) && cell.getStack().contains(this)) {
                break;
            }
            cell.getStack().push(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringSnake = new StringBuilder();

        for (var point : this.snake) {
            if (!point.equals(this.getHead())) stringSnake.append(" -> ");

            stringSnake.append(point);
        }

        stringSnake.append('\n');

        return stringSnake.toString();
    }
}
