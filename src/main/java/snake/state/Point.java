package snake.state;

import java.util.Comparator;

public interface Point extends Comparable<Point>, Comparator<Point> {
    public int getX();

    public int getY();

    default int getIndex(int width) {
        return getY() * width + getX();
    }

    @Override
    default int compareTo(Point other) {
        return compare(this, other);
    }

    @Override
    default int compare(Point p1, Point p2) {
        if (p1.getX() != p2.getX()) {
            return Integer.compare(p1.getX(), p2.getX());
        } else {
            return Integer.compare(p1.getY(), p2.getY());
        }
    }

    default boolean equals(Point other) {
        return this.compareTo(other) == 0;
    }
}
