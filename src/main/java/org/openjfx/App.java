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

    @Override
    public void start(Stage stage) throws InterruptedException {
        SceneManager.getInstance().setStage(stage);

        stage.setWidth(800);
        stage.setHeight(600);


        var state = new State();
        var board = new Board(20, 20, state);
        var snake = new Snake(new Point(10, 10), Direction.UP);
        state.getGameObjects().add(snake);
        var snakeScreen = new SnakeScene(state, snake);
        snakeScreen.setActive();
        stage.show();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    board.build();
                    state.getGameObjects().forEach(GameObject::step);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}