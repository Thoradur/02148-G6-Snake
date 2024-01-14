package org.openjfx.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import org.openjfx.SceneProvider;
import snake.common.Direction;
import snake.node.Player;
import snake.state.Snake;
import snake.state.State;

public class SnakeScene implements SceneProvider {
    private final Scene scene;
    private final Group root = new Group();
    private final Player player;
    private final SnakeCanvas snakeCanvas;

    public SnakeScene(Player player) {
        this.player = player;
        this.scene = new Scene(root);
        this.snakeCanvas = new SnakeCanvas(player);
        this.snakeCanvas.setScene(scene);
        this.root.getChildren().add(snakeCanvas.getNode());

        

        this.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case P -> player.getSnake().grow(3);
                default -> {}
            };

            var nextDirection = switch (e.getCode()) {
                case UP, W -> Direction.UP;
                case LEFT, A -> Direction.LEFT;
                case DOWN, S -> Direction.DOWN;
                case RIGHT, D -> Direction.RIGHT;
                default -> null;
            };

            System.out.println("Current direction: " + player.getSnake().getDirection() + ", next direction: " + nextDirection);

            if (nextDirection != null && !player.getSnake().getDirection().isOpposite(nextDirection)) {
                player.getSnake().setDirection(nextDirection);
                System.out.println("Changing direction to " + nextDirection);
            }
        });
    }

    public SnakeCanvas getSnakeCanvas() {
        return snakeCanvas;
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
