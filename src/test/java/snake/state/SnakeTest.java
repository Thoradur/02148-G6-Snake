package snake.state;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import snake.common.Direction;
import snake.common.Point;

import java.util.Random;

public class SnakeTest {

    @RepeatedTest(100)
    public void testSnakeDehydration() {
        int steps = 40;

        Random random = new Random();

        Snake s1 = new Snake(Point.random(random), Direction.random(random));

        for (int i = 0; i < steps; i++) {
            s1.grow(random.nextInt(3));
            s1.step();
            s1.setDirection(Direction.random(random));
        }

        var dehydratedSnake = s1.getDehydratedSnake();

        Snake s2 = new Snake(dehydratedSnake);

        Assertions.assertEquals(s1.size(), s2.size());
    }
}
