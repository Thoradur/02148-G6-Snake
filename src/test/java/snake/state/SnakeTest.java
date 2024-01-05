package snake.state;

import org.junit.Assert;
import org.junit.Test;
import snake.state.*;

import java.util.Arrays;
import java.util.List;

public class SnakeTest {
    @Test
    public void testSnake() {
        // Small 3x3 board
        Board board = new Board(3, 3);

        // Snake starting at (0, 0)
        Snake snake = new Snake(board.getCell(0, 0));

        // Give it room to grow
        snake.grow(3);

        Assert.assertEquals(1, snake.length());

        // Move snake to (1, 0)
        snake.move(board.getCell(1, 0));

        Assert.assertEquals(2, snake.length());

        // Move snake to (2, 0)

        snake.move(board.getCell(2, 0));

        Assert.assertEquals(3, snake.length());

        // Move snake to (2, 1)

        snake.move(board.getCell(2, 1));

        Assert.assertEquals(4, snake.length());

        System.out.println(board);

        List<Point> compactSnake = snake.getCompactSnake();

        System.out.println(Arrays.toString(compactSnake.toArray()));
        Assert.assertTrue(compactSnake.get(0).equals(new PointRef(2, 1)));
        Assert.assertTrue(compactSnake.get(1).equals(new PointRef(2, 0)));
        Assert.assertTrue(compactSnake.get(2).equals(new PointRef(0, 0)));

        board.destroy();

        Assert.assertEquals(0, snake.size());
        
        Assert.assertEquals(3, compactSnake.size());

        Snake recoveredSnake = Snake.createSnakeFromCompactSnake(board, compactSnake);

        System.out.println(board);

        Assert.assertEquals(4, recoveredSnake.length());
    }
}
