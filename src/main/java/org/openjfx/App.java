package org.openjfx;

import javafx.scene.Group;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage){
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var startButton = new Button("Start");
        startButton.setOnAction(e -> {
            System.out.println("Start button pressed");
            stage.close();
            new SnakeGame().start(new Stage());
        });

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.add(label, 0, 0);
        root.add(startButton, 0, 1);



        var scene = new Scene(root, 640, 480);

        stage.setScene(scene);
        stage.setTitle("SnakeGame " + javafxVersion);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}