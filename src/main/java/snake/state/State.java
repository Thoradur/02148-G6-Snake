package snake.state;

import snake.protocol.state.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class State {
    private final Random random;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Stack<Layer> layers = new Stack<>();

    private Board board = null;

    public State() {
        this.random = new Random();
    }

    public State(int seed) {
        this.random = new Random(seed);
    }

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
