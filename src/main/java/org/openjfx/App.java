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
        SceneManager.getInstance().setStage(stage);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();

        var startScreen = new StartScreenScene();
        startScreen.setActive();
    }

    public static void main(String[] args) {
        launch();
    }
}