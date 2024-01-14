package snake.state;

import snake.protocol.state.StateUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.function.Predicate;

public class State {
    private int step = 0;
    private int currentSeed = 0;
    private final Random baseRandom;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Stack<Layer> layers = new Stack<>();

    private Board board = null;

    public State() {
        this.baseRandom = new Random();
    }

    public State(int seed) {
        this.baseRandom = new Random(seed);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public int getStep() {
        return step;
    }

    public Random getRandom() {
        return new Random(currentSeed);
    }

    public void step(Predicate<GameObject> predicate) {
        // Derive a new seed from the initial seed.
        currentSeed = baseRandom.nextInt();
        step++;

        gameObjects.stream().filter(predicate).forEach(GameObject::step);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void apply(StateUpdate fragment) {
        var layer = layers.peek();

        layer.add(fragment);
    }

    public void unapply(StateUpdate fragment) {
        var layer = layers.peek();

        layer.remove(fragment);
    }
}
