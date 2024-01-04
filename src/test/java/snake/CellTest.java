package snake;

import org.junit.Assert;
import org.junit.Test;
import snake.state.Cell;

public class CellTest {
    @Test
    public void testCell() {
        Cell cell = new Cell(0, 0);
        Assert.assertEquals(0, cell.getX());
        Assert.assertEquals(0, cell.getY());
    }
}
