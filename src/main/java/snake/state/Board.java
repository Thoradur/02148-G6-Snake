package snake.state;

import snake.common.Point;

import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * A view of the state.
 */
public class Board {
    private int width;
    private int height;
    private final Cell[][] cells;
    private final State state;

    public Board(int width, int height, State state) {
        this.width = width;
        this.height = height;
        this.state = state;
        state.setBoard(this);
        cells = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; ++y) {
                cells[x][y] = new Cell(new Point(x, y));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void build() {
        // Clear all cells
        stream().map(Cell::getStack).forEach(Stack::clear);

        // Build all game objects
        for (var gameObject : state.getGameObjects()) {
            gameObject.build(this);
        }
    }

    public Stream<Cell> stream() {
        return Stream.of(cells).flatMap(Stream::of);
    }

    public Cell getCell(Point point) {
        // If cell is out of bounds, wrap it around
        return cells[point.x()][point.y()];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                sb.append(cells[x][y].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
