package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

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
import snake.state.Fruit;

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
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        var textField = new TextField();
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        var startButton = new Button("Start");
        startButton.disableProperty().bind(textField.textProperty().isEmpty());
        startButton.setOnAction(e -> {
            try {
                App.port = Integer.parseInt(textField.getText());
                System.out.println(port);
                startGame(stage);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.add(label, 0, 0);
        root.add(startButton, 0, 1);
        root.add(textField, 0, 2);

        var scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.setTitle("SnakeGame " + javafxVersion);
        stage.show();
    }

    public void startGame(Stage stage) throws InterruptedException {
        SceneManager.getInstance().setStage(stage);

        stage.setWidth(800);
        stage.setHeight(600);

        URI baseUri = URI.create("tcp://localhost:" + port + "/?keep");
        Point[] startSnake1 = new Point[] { new Point(1, 10) };
        Point[] startSnake2 = new Point[] { new Point(1, 20) };
        Point[] startSnake;

        OpponentInfo[] opponents = new OpponentInfo[1];

        if (port == 2020) {
            startSnake = startSnake1;
            opponents[0] = new OpponentInfo(URI.create("tcp://localhost:3030/?keep"), Direction.RIGHT, startSnake2,
                    "secret", "secret");

        } else if (port == 3030) {
            startSnake = startSnake2;
            opponents[0] = new OpponentInfo(URI.create("tcp://localhost:2020/?keep"), Direction.RIGHT, startSnake1,
                    "secret", "secret");
        } else {
            throw new RuntimeException("Invalid port must be 8080 or 8081");
        }

        var startGame = new StartGame("player_id", 50, 50, 10, Direction.RIGHT, startSnake, opponents);
        var player = new Player(baseUri, startGame);

        var fruit = new Fruit(new Point(10, 10));

        var snakeScreen = new SnakeScene(player);
        snakeScreen.setActive();
        stage.show();

        new Thread(player).start();
    }

    public static void main(String[] args) {

        launch();
    }
}