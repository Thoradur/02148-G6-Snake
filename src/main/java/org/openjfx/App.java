package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import snake.common.Direction;
import snake.common.Point;
import snake.node.OpponentNode;
import snake.node.Player;
import snake.state.Board;
import snake.state.Snake;
import snake.state.State;

import java.io.IOException;
import java.net.URI;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var startButton = new Button("Start");

        //Text box
        TextArea myPort = new TextArea();
        TextArea theirPort = new TextArea();


        startButton.setOnAction(e -> {
            System.out.println("Start button pressed");
            System.out.println(myPort.getText() + " : " + theirPort.getText());
            stage.close();


            State state = new State();
            Board board = new Board(600 / 25, 600 / 25, state);
            ;

            Snake me = new Snake(new Point(0, 0), Direction.DOWN);
            Snake them = new Snake(new Point(10, 10), Direction.RIGHT);

            state.getGameObjects().add(me);
            state.getGameObjects().add(them);

            var myUri = URI.create("tcp://localhost:" + myPort.getText().strip() + "/?keep");
            var theirUri = URI.create("tcp://localhost:" + theirPort.getText().strip() + "/opponent" + theirPort.getText().strip() + "?keep");

            var self = new Player(myUri, state, me);

            OpponentNode opponent = null;

            try {
                opponent = new OpponentNode(theirUri, state, them);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            new Thread(self).start();
            new Thread(opponent).start();

            // Start UI.
            new SnakeGame(state, board, me).start(new Stage());
        });

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.add(label, 0, 0);
        root.add(startButton, 0, 2);
        root.add(myPort, 0, 1);
        root.add(theirPort, 1, 1);


        var scene = new Scene(root, 640, 480);

        stage.setScene(scene);
        stage.setTitle("SnakeGame " + javafxVersion);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}