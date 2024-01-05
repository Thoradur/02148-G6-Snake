package snake.state;

import java.util.Stack;

public final class Cell implements BoardEntity, Point {
    private final Stack<CellEntity> stack = new Stack<>();

    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public Stack<CellEntity> getStack() {
        return stack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Iterable<CellEntity> getCells() {
        return stack;
    }

    @Override
    public void destroy() {
        while (!stack.isEmpty()) {
            stack.pop().destroy();
        }
    }

    @Override
    public String toString() {
        // # is a snake
        // * is food
        // . is an empty cell

        if (stack.isEmpty())
            return ".";


        if (stack.peek() instanceof SnakeCell snakeCell)
            return snakeCell.isHead() ? "H" : (snakeCell.isTail() ? "T" : "#");

        return "*";
    }
}
