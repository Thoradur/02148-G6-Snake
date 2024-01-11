package org.openjfx.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import org.openjfx.SceneProvider;
import snake.common.Direction;
import snake.state.Snake;
import snake.state.State;

public class SnakeScene implements SceneProvider {
    private final Scene scene;
    private final Group root = new Group();
    private final State state;
    private final Snake playerSnake;
    private final SnakeCanvas snakeCanvas;

    public SnakeScene(State state, Snake playerSnake) {
        this.scene = new Scene(root);
        this.state = state;
        this.playerSnake = playerSnake;
        this.snakeCanvas = new SnakeCanvas(this.state);
        this.snakeCanvas.setScene(scene);
        this.root.getChildren().add(snakeCanvas.getNode());

        this.scene.setOnKeyPressed(e -> {
            var nextDirection = switch (e.getCode()) {
                case UP, W -> Direction.UP;
                case LEFT, A -> Direction.LEFT;
                case DOWN, S -> Direction.DOWN;
                case RIGHT, D -> Direction.RIGHT;
                default -> null;
            };

            System.out.println("Current direction: " + playerSnake.getDirection() + ", next direction: " + nextDirection);

            if (nextDirection != null && !playerSnake.getDirection().isOpposite(nextDirection)) {
                playerSnake.setDirection(nextDirection);
                System.out.println("Changing direction to " + nextDirection);
            }
        });
    }

    @Override
    public String getTitle() {
        return "SnakeGame";
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
