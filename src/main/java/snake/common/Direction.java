package snake.common;

import java.util.Random;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }

    public Point point() {
        return new Point(dx, dy);
    }

    public boolean isOpposite(Direction other) {
        return dx == -other.dx && dy == -other.dy;
    }

    public static Direction fromPoints(Point p1, Point p2) {
        if (p1.x() != p2.x()) {
            return p1.x() < p2.x() ? RIGHT : LEFT;
        }

        return p1.y() < p2.y() ? DOWN : UP;
    }

    public static Direction random(Random rng) {
        return values()[rng.nextInt(values().length)];
    }
}
