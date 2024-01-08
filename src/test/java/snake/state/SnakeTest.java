package snake.state;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import snake.common.Direction;
import snake.common.Point;

import java.util.Random;

public class SnakeTest {

    @RepeatedTest(10)
    public void testSnakeDehydration() {
        int steps = 10;

        Random random = new Random();

        Snake s1 = new Snake(new Point(random.nextInt(10), random.nextInt(10)), Direction.random(random));

        for (int i = 0; i < steps; i++) {
            s1.grow(random.nextInt(3));
            s1.setDirection(Direction.random(random));
        }

        var dehydratedSnake = s1.getDehydratedSnake();

        System.out.println(dehydratedSnake);

        Snake s2 = new Snake(dehydratedSnake);

        Assertions.assertEquals(s1.size(), s2.size());
    }
}
