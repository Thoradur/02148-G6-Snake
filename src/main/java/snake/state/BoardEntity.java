package snake.state;

import java.util.Iterator;

/**
 * Every entity that can exist on the board must implement this interface.
 */
public interface BoardEntity {
    public Iterable<CellEntity> getCells();

    public void destroy();
}
