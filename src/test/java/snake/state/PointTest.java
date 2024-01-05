package snake.state;

import org.junit.Assert;
import org.junit.Test;

public class PointTest {
    @Test
    public void testPoint() {
        Point point = new PointRef(0, 0);

        Assert.assertEquals(0, point.getX());
        Assert.assertEquals(0, point.getY());

        Point point_1 = new PointRef(1, 0);

        Assert.assertEquals(1, point_1.getX());
        Assert.assertEquals(0, point_1.getY());

        Point point_2 = new PointRef(0, 1);

        Assert.assertEquals(0, point_2.getX());
        Assert.assertEquals(1, point_2.getY());

        Point point_3 = new PointRef(1, 1);

        Assert.assertEquals(1, point_3.getX());
        Assert.assertEquals(1, point_3.getY());

        Assert.assertEquals(0, point.compareTo(point));
        Assert.assertEquals(0, point_1.compareTo(point_1));
        Assert.assertEquals(0, point_2.compareTo(point_2));
        Assert.assertEquals(0, point_3.compareTo(point_3));

        Assert.assertEquals(-1, point.compareTo(point_1));
        Assert.assertEquals(-1, point.compareTo(point_2));
        Assert.assertEquals(-1, point.compareTo(point_3));

        Assert.assertEquals(1, point_1.compareTo(point));
        Assert.assertEquals(1, point_1.compareTo(point_2));
        Assert.assertEquals(-1, point_1.compareTo(point_3));

        Assert.assertEquals(1, point_2.compareTo(point));
        Assert.assertEquals(-1, point_2.compareTo(point_1));
        Assert.assertEquals(-1, point_2.compareTo(point_3));

        Assert.assertEquals(1, point_3.compareTo(point));
        Assert.assertEquals(1, point_3.compareTo(point_1));
        Assert.assertEquals(1, point_3.compareTo(point_2));
    }

    @Test
    public void testInterpolate() {
        Point point = new PointRef(0, 0);
        Point point_1 = new PointRef(1, 0);
        Point point_2 = new PointRef(2, 0);
        Point point_3 = new PointRef(3, 0);
        Point point_4 = new PointRef(4, 0);
        Point point_5 = new PointRef(5, 0);
        Point point_6 = new PointRef(6, 0);
        Point point_7 = new PointRef(7, 0);
        Point point_8 = new PointRef(8, 0);
        Point point_9 = new PointRef(9, 0);
        Point point_10 = new PointRef(10, 0);

        Assert.assertEquals(2, PointRef.interpolate(point, point_1).size());
        Assert.assertEquals(3, PointRef.interpolate(point, point_2).size());
        Assert.assertEquals(4, PointRef.interpolate(point, point_3).size());

        // Assert that the interpolation between point and point_10 gives all the points in between
        Assert.assertEquals(11, PointRef.interpolate(point, point_10).size());

        var interpolation = PointRef.interpolate(point, point_10);

        Assert.assertTrue(point.equals(interpolation.get(0)));
        Assert.assertTrue(point_1.equals(interpolation.get(1)));
        Assert.assertTrue(point_2.equals(interpolation.get(2)));
        Assert.assertTrue(point_3.equals(interpolation.get(3)));
        Assert.assertTrue(point_4.equals(interpolation.get(4)));
        Assert.assertTrue(point_5.equals(interpolation.get(5)));
        Assert.assertTrue(point_6.equals(interpolation.get(6)));
        Assert.assertTrue(point_7.equals(interpolation.get(7)));
        Assert.assertTrue(point_8.equals(interpolation.get(8)));
        Assert.assertTrue(point_9.equals(interpolation.get(9)));
        Assert.assertTrue(point_10.equals(interpolation.get(10)));
    }

    @Test
    public void testInterpolateY() {
        Point point = new PointRef(0, 0);
        Point point_1 = new PointRef(0, 1);
        Point point_2 = new PointRef(0, 2);
        Point point_3 = new PointRef(0, 3);
        Point point_4 = new PointRef(0, 4);
        Point point_5 = new PointRef(0, 5);
        Point point_6 = new PointRef(0, 6);
        Point point_7 = new PointRef(0, 7);
        Point point_8 = new PointRef(0, 8);
        Point point_9 = new PointRef(0, 9);
        Point point_10 = new PointRef(0, 10);

        Assert.assertEquals(2, PointRef.interpolate(point, point_1).size());
        Assert.assertEquals(3, PointRef.interpolate(point, point_2).size());
        Assert.assertEquals(4, PointRef.interpolate(point, point_3).size());

        // Assert that the interpolation between point and point_10 gives all the points in between
        Assert.assertEquals(11, PointRef.interpolate(point, point_10).size());

        var interpolation = PointRef.interpolate(point, point_10);

        Assert.assertTrue(point.equals(interpolation.get(0)));
        Assert.assertTrue(point_1.equals(interpolation.get(1)));
        Assert.assertTrue(point_2.equals(interpolation.get(2)));
        Assert.assertTrue(point_3.equals(interpolation.get(3)));
        Assert.assertTrue(point_4.equals(interpolation.get(4)));
        Assert.assertTrue(point_5.equals(interpolation.get(5)));
        Assert.assertTrue(point_6.equals(interpolation.get(6)));
        Assert.assertTrue(point_7.equals(interpolation.get(7)));
        Assert.assertTrue(point_8.equals(interpolation.get(8)));
        Assert.assertTrue(point_9.equals(interpolation.get(9)));
        Assert.assertTrue(point_10.equals(interpolation.get(10)));
    }

    @Test
    public void testInterpolateXYThrows() {
        Point point = new PointRef(0, 0);
        Point point_1 = new PointRef(1, 1);

        Assert.assertThrows(IllegalArgumentException.class, () -> PointRef.interpolate(point, point_1));
    }
}
