package snake.state;

import snake.common.Point;

import java.util.Stack;

public class Cell {
    public Point position;
    private Stack<GameObject> stack = new Stack<>();

    public Cell(Point position) {
        this.position = position;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public Stack<GameObject> getStack() {
        return stack;
    }

    @Override
    public String toString() {
        if (stack.isEmpty()) return ".";

        var top = stack.peek();

        if (top instanceof Snake snake) {
            if (snake.getHead().equals(position)) return "H";

            if (snake.getTail().equals(position)) return "T";

            return "#";
        }

        return "?";
    }
}
