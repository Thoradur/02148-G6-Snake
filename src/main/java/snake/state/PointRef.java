package snake.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointRef implements Point {
    private final int x;
    private final int y;

    private static List<Point> generatePoints(int fixed, int start, int end, boolean isVertical) {
        return IntStream.rangeClosed(start, end)
                .mapToObj(i -> isVertical ? new PointRef(fixed, i) : new PointRef(i, fixed))
                .collect(Collectors.toList());
    }

    public static List<Point> interpolate(Point p1, Point p2) {
        if (p1.getX() == p2.getX()) {
            // Points are in the same column, interpolate vertically
            return generatePoints(p1.getX(), Math.min(p1.getY(), p2.getY()), Math.max(p1.getY(), p2.getY()), true);
        }

        if (p1.getY() == p2.getY()) {
            // Points are in the same row, interpolate horizontally
            return generatePoints(Math.min(p1.getX(), p2.getX()), p1.getY(), Math.max(p1.getX(), p2.getX()), false);
        }

        throw new IllegalArgumentException("Points are not in the same row or column: " + p1 + ", " + p2);
    }

    public PointRef(Point point) {
        this(point.getX(), point.getY());
    }

    public PointRef(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return getIndex(1000);
    }

    @Override
    public String toString() {
        return String.format("PointRef(%d, %d)", getX(), getY());
    }
}
