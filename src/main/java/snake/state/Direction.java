package snake.state;

import java.util.Comparator;

public enum Direction implements Comparable<Direction>, Comparator<Direction> {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point point) {
        return new PointRef(point.getX() + x, point.getY() + y);
    }

    public static Direction betweenPoints(Point p1, Point p2) {
        if (p1.getX() != p2.getX()) {
            return p1.getX() < p2.getX() ? RIGHT : LEFT;
        } else {
            return p1.getY() < p2.getY() ? UP : DOWN;
        }
    }

    @Override
    public int compare(Direction direction, Direction t1) {
        return Integer.compare(direction.ordinal(), t1.ordinal());
    }
}
