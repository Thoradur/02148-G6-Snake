package snake.state;

import org.junit.jupiter.api.Test;
import snake.common.Direction;
import snake.common.Point;

import java.util.ArrayList;
import java.util.List;

public class DirectionTest {
    @Test
    public void testDirection() {
        var s1_p1 = new Point(5, 5);
        var s1_p2 = new Point(6, 5);
        var s1_p3 = new Point(6, 4);

        var s1 = new Snake(List.of(s1_p1, s1_p2, s1_p3));

        var s2_p1 = new Point(3, 2);
        var s2_p2 = new Point(4, 2);
        var s2_p3 = new Point(4, 2);

//        var s1 = new Snake(List.of(s2_p1, s2_p2, s2_p3));

        System.out.println(s1);
        System.out.println(s1.getDehydratedSnake());
    }
}
