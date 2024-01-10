package snake.state;

import snake.protocol.state.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Stack<Layer> layers = new Stack<>();

    private Board board = null;

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void apply(Fragment fragment) {
        var layer = layers.peek();

        layer.add(fragment);
    }

    public void unapply(Fragment fragment) {
        var layer = layers.peek();

        layer.remove(fragment);
    }
}
