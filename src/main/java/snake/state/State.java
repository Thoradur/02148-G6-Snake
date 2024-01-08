package snake.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Stack<Layer> layers = new Stack<>();

    public List<GameObject> getGameObjects() {
        return gameObjects;
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
