package org.openjfx.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.openjfx.SceneProvider;
import org.openjfx.scoreboard.ScoreboardNode;
import snake.common.Direction;
import snake.coordination.CoordinationLobby;
import snake.node.Player;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.LeaveLobby;
import snake.protocol.coordination.PlayerInfo;
import snake.protocol.coordination.Ready;
import snake.state.Snake;
import snake.state.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SnakeScene implements SceneProvider {
    private final Scene scene;
    private final BorderPane root = new BorderPane();
    private final Player player;
    private final SnakeCanvas snakeCanvas;
    private final ScoreboardNode scoreboardNode;
    private final Space space = new SequentialSpace();

    public SnakeScene(Player player) {
        this.player = player;
        this.scene = new Scene(root);
        this.snakeCanvas = new SnakeCanvas(player);

        //making new instance of scoreboard
        int[] playerScore = {1,3};
        //Scoreboard receives the list of opponents (string) and playerscore
        this.scoreboardNode = new ScoreboardNode(player.getOpponentNames(),playerScore);
        System.out.println(player.getOpponentNames());

        //setting the scenes
        this.snakeCanvas.setScene(scene);
        this.scoreboardNode.setScene(scene);




        Canvas canvas = (Canvas) snakeCanvas.getNode();
        this.root.setCenter(canvas);
        this.root.setTop(scoreboardNode.getNode());


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
