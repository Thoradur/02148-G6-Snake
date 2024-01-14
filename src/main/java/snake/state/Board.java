package snake.state;

import snake.common.Point;

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
        state.getGameObjects().forEach(gameObject -> gameObject.build(this));

        collisionCheck();
        removeEatenFruit();
        spawnFruitAlgorithm();
    }

    private void collisionCheck() {
        // Find all non-dead snakes
        state.getGameObjects().stream().filter(o -> {
            if (o instanceof Snake s) {
                return !s.isDead();
            }

            return false;
        }).forEach(o -> {
            var snake = (Snake) o;

            // Skip already dead snakes.
            if (snake.isDead()) return;

            var head = snake.getHead();
            var headCell = getCell(head);

            // Get copy
            var stack = (Stack<?>) headCell.getStack().clone();

            // Filter out first element
            stack.stream().filter(gameObject -> gameObject == snake).findFirst().ifPresent(stack::remove);

            // Check if any snakes are in the same cell as the head
            var additionalSnakes = stack.stream().filter(go -> go instanceof Snake).map(go -> (Snake) go).toList();

            for (var otherSnake : additionalSnakes) {
                // If we hit any other snake in their heads, kill them
                if (otherSnake.getHead().equals(head)) {
                    System.out.println("Snake has collided with another snake");
                    otherSnake.kill();
                }
            }

            // If any other snake block is on the same block
            // as our head, we die.
            if (!additionalSnakes.isEmpty()) {
                System.out.println("Snake has collided with another snake");

                // Give points to the top snake
                var topSnake = additionalSnakes.getFirst();
                topSnake.grow(snake.size());

                snake.kill();
                return;
            }

            // Find all fruits in the same cell as the head
            var fruits = stack.stream().filter(go -> go instanceof Fruit).map(go -> (Fruit) go).toList();

            // Eat fruits we can eat.
            for (var fruit : fruits) {
                if (fruit.isEaten()) continue;

                fruit.eat();
                snake.grow(fruit.getPoints());
            }
        });
    }

    private void spawnFruitAlgorithm() {
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

            // TODO: More fruits with rarities.
            var fruit = new Apple(randomCell.position);

            this.state.getGameObjects().add(fruit);
        }
    }

    private void removeEatenFruit() {
        // Find all fruits
        var fruits = state.getGameObjects().stream().filter(gameObject -> gameObject instanceof Fruit).map(gameObject -> (Fruit) gameObject).toList();

        // Remove all eaten fruits
        fruits.stream().filter(Fruit::isEaten).forEach(state.getGameObjects()::remove);
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
