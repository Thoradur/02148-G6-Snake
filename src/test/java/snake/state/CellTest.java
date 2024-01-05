package snake.state;

import org.junit.Assert;
import org.junit.Test;
import snake.state.Cell;
import snake.state.CellEntity;
import snake.state.Snake;
import snake.state.SnakeCell;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class CellTest {
    @Test
    public void testCell() {
        Cell cell = new Cell(0, 0);
        Cell cell_1 = new Cell(1, 0);

        Assert.assertEquals(0, cell.getX());
        Assert.assertEquals(0, cell.getY());

        // Snake starting at (0, 0)
        Snake snake = new Snake(cell);

        Assert.assertEquals(1, snake.size());

        Assert.assertEquals(1, cell.getStack().size());

        snake.grow(10);

        Assert.assertEquals(11, snake.size());

        Assert.assertEquals(11, cell.getStack().size());

        snake.move(cell_1);

        Assert.assertEquals(11, snake.size());

        Assert.assertEquals(10, cell.getStack().size());
        Assert.assertEquals(1, cell_1.getStack().size());

        snake.grow(-5);

        Assert.assertEquals(6, snake.size());

        Assert.assertEquals(5, cell.getStack().size());
        Assert.assertEquals(1, cell_1.getStack().size());

        Optional<CellEntity> list = StreamSupport.stream(cell_1.getCells().spliterator(), false).findFirst();

        Assert.assertTrue(list.isPresent());

        Assert.assertTrue(list.get() instanceof SnakeCell);
        Assert.assertTrue(((SnakeCell) list.get()).isHead());

        snake.destroy();

        Assert.assertEquals(0, snake.size());
        Assert.assertEquals(0, cell.getStack().size());
        Assert.assertEquals(0, cell_1.getStack().size());
    }
}
