package org.openjfx;

import javafx.stage.Stage;

public class SceneManager {
    private final static SceneManager instance = new SceneManager();

    public static SceneManager getInstance() {
        return instance;
    }

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
