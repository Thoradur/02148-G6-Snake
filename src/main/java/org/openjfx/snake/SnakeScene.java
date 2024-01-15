package org.openjfx.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.openjfx.SceneProvider;
import snake.common.Direction;
import snake.node.Player;
import snake.state.Snake;
import snake.state.State;

import java.util.Objects;

public class SnakeScene implements SceneProvider {
    private final Scene scene;
    private final BorderPane root = new BorderPane();
    private final Player player;
    private final SnakeCanvas snakeCanvas;

    public SnakeScene(Player player) {
        this.player = player;
        this.scene = new Scene(root);
        this.snakeCanvas = new SnakeCanvas(player);
        this.snakeCanvas.setScene(scene);
        Canvas canvas = (Canvas) snakeCanvas.getNode();
        this.root.setCenter(canvas);

        this.root.prefWidthProperty().bind(canvas.widthProperty());
        this.root.prefHeightProperty().bind(canvas.heightProperty());

        this.scene.setOnKeyPressed(e -> {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.P) {
                player.getSnake().grow(3);
            }

            var nextDirection = switch (e.getCode()) {
                case UP, W -> Direction.UP;
                case LEFT, A -> Direction.LEFT;
                case DOWN, S -> Direction.DOWN;
                case RIGHT, D -> Direction.RIGHT;
                default -> null;
            };

            if (nextDirection != null && !player.getSnake().getDirection().isOpposite(nextDirection)) {
                player.getSnake().setDirection(nextDirection);
                // System.out.println("Changing direction to " + nextDirection);
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
