import snake.state.Direction;
import snake.state.Snake;
import snake.state.Board;

public class CliSnake {
    public static void main(String[] args) {
        Board board = new Board(10, 20);
        Snake snake = new Snake(board.getCell(0, 0));
        Direction direction = Direction.RIGHT;

        while (true) {
            System.out.println("-----------------------------------------------------\n\n");
            System.out.println(board);

            // Apply direction
            var nextPoint = direction.add(snake.getHead());

            try {
                snake.move(board.getCell(nextPoint));
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            // Get user input to perhaps change direction
            var input = System.console().readLine();

            if (input == null) {
                break;
            }

            switch (input) {
                case "g":
                    snake.grow(1);
                    break;
                case "w":
                    direction = Direction.UP;
                    break;
                case "a":
                    direction = Direction.LEFT;
                    break;
                case "s":
                    direction = Direction.DOWN;
                    break;
                case "d":
                    direction = Direction.RIGHT;
                    break;
                default:
                    System.out.println("Invalid input");
            }

        }
    }
}
