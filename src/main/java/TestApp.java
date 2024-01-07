import snake.common.Direction;
import snake.common.Point;
import snake.state.Snake;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TestApp {
    public static void main(String[] args) {
        Snake snake = new Snake(new Point(1, 1), Direction.RIGHT);

        snake.move();
        snake.move();

        snake.setDirection(Direction.DOWN);

        snake.move();
        snake.move();

        snake.grow(3);

        snake.setDirection(Direction.RIGHT);

        snake.move();
        snake.move();
        snake.move();

        var dehydratedSnake = snake.getDehydratedSnake();

        Snake snake2 = new Snake(dehydratedSnake);

        System.out.println(snake);
        System.out.println(snake2);
    }
}
