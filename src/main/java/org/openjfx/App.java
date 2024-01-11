package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.openjfx.snake.SnakeScene;
import org.openjfx.startscreen.StartScreenScene;
import snake.common.Direction;
import snake.common.Point;
import snake.coordination.CoordinationLobby;
import snake.coordination.CoordinationServer;
import snake.node.OpponentNode;
import snake.node.Player;
import snake.protocol.coordination.OpponentInfo;
import snake.protocol.coordination.StartGame;
import snake.state.Board;
import snake.state.GameObject;
import snake.state.Snake;
import snake.state.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * JavaFX App
 */
public class App extends Application {
    private static int port;

    @Override
    public void start(Stage stage) throws InterruptedException {
        SceneManager.getInstance().setStage(stage);

        stage.setWidth(800);
        stage.setHeight(600);

        URI baseUri = URI.create("tcp://localhost:" + port + "/?keep");
        Point[] startSnake1 = new Point[]{new Point(10, 10)};
        Point[] startSnake2 = new Point[]{new Point(20, 20)};
        Point[] startSnake;

        OpponentInfo[] opponents = new OpponentInfo[1];

        if (port == 8081) {
            startSnake = startSnake1;
            opponents[0] = new OpponentInfo(URI.create("tcp://localhost:8080/?keep"), Direction.DOWN, startSnake2, "secret", "secret");

        } else if (port == 8080) {
            startSnake = startSnake2;
            opponents[0] = new OpponentInfo(URI.create("tcp://localhost:8081/?keep"), Direction.DOWN, startSnake1, "secret", "secret");
        } else {
            throw new RuntimeException("Invalid port must be 8080 or 8081");
        }


        var startGame = new StartGame("player_id", 50, 50, 10, Direction.DOWN, startSnake, opponents);
        var player = new Player(baseUri, startGame);

        var snakeScreen = new SnakeScene(player);
        snakeScreen.setActive();
        stage.show();

        new Thread(player).start();
    }

    public static void main(String[] args) {
        App.port = 8080;
        launch();
    }
}