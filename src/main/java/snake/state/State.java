package snake.state;

import java.util.Stack;

public class State {
    private final Stack<Layer> layers = new Stack<>();

    public void apply(Fragment fragment) {
        var layer = layers.peek();

        layer.add(fragment);
    }

    public void unapply(Fragment fragment) {
        var layer = layers.peek();

        layer.remove(fragment);
    }
}
