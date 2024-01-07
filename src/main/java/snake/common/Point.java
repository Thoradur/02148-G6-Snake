package snake.common;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Point(int x, int y) implements Comparable<Point>, Comparator<Point> {
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point add(Direction direction) {
        return this.add(direction.point());
    }

    public Stream<Point> interpolateTo(Point other) {
        boolean isVertical = this.x == other.x;
        boolean isHorizontal = this.y == other.y;

        if (!(isVertical || isHorizontal)) {
            throw new IllegalArgumentException("Points are not in the same row or column: " + this + ", " + other);
        }

        int a = isVertical ? this.y : this.x;
        int b = isVertical ? other.y : other.x;
        int fixed = isVertical ? this.x : this.y;
        boolean reversed = a > b;

        return IntStream.range(Math.min(a, b), Math.max(a, b)).map(i -> reversed ? b - i + a : i).mapToObj(i -> isVertical ? new Point(fixed, i) : new Point(i, fixed));
    }

    public int distanceTo(Point other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    @Override
    public int compareTo(Point point) {
        return this.compare(this, point);
    }

    @Override
    public int compare(Point point, Point t1) {
        if (point.x != t1.x) {
            return Integer.compare(point.x, t1.x);
        } else {
            return Integer.compare(point.y, t1.y);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point point) {
            return this.compareTo(point) == 0;
        }

        return false;
    }

    public static Point random(Random rng) {
        return new Point(rng.nextInt(), rng.nextInt());
    }
}
