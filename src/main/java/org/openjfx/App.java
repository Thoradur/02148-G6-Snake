package org.openjfx;

import javafx.application.Application;
import javafx.stage.Stage;

import org.openjfx.startscreen.StartScreenScene;

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