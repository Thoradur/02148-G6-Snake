package snake.state;

import java.util.ArrayList;
import java.util.Collections;

public abstract class CellEntity implements BoardEntity, Point {
    private final Cell parent;

    public CellEntity(Cell parent) {
        this.parent = parent;
        parent.getStack().push(this);
    }

    public Cell getParent() {
        return parent;
    }

    @Override
    public int getX() {
        return parent.getX();
    }

    @Override
    public int getY() {
        return parent.getY();
    }

    @Override
    public Iterable<CellEntity> getCells() {
        // iterate single cell
        return Collections.singleton(this);
    }

    @Override
    public void destroy() {
        parent.getStack().remove(this);
    }
}
