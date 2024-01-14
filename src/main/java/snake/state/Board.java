package snake.state;

import snake.common.Point;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * A view of the state.
 */
public class Board {
    private final int width;
    private final int height;
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

    public State getState() {
        return state;
    }

    public void build() {
        // Clear all cells
        stream().map(Cell::getStack).forEach(Stack::clear);

        // Build all game objects
        for (var gameObject : state.getGameObjects()) {
            gameObject.build(this);
        }

        for (var gameObject : state.getGameObjects()) {
            if (gameObject instanceof Snake s) {
                s.collision(this);
            }
        }

        // Count the amount of fruits on the board
        var fruitCount = state.getGameObjects().stream().filter(gameObject -> gameObject instanceof Fruit).count();

        // If there are less than 3 fruits on the board, begin the process of deciding where to spawn a new fruit.
        if (fruitCount > 3) return;

        // Find all empty cells
        var emptyCells = stream().filter(cell -> cell.getStack().isEmpty()).toList();

        Random currentRandom = state.getRandom();

        // These fruit (between 0 and 3) will be spawned next step.
        var fruitsToSpawn = currentRandom.nextInt(3);

        for (int i = 0; i < fruitsToSpawn; i++) {
            // Pick a random empty cell
            var randomCell = emptyCells.get(currentRandom.nextInt(emptyCells.size()));

            var fruit = new Fruit(randomCell.position);

            this.state.getGameObjects().add(fruit);
        }
    }

    public Stream<Cell> stream() {
        return Stream.of(cells).flatMap(Stream::of);
    }

    public Cell getCell(Point point) {
        try {
            return cells[point.x()][point.y()];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
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
