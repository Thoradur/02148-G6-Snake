package snake.state;

public class PointRef implements Point {
    private final int x;
    private final int y;

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
