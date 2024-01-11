package org.openjfx;

import javafx.scene.Scene;

public interface SceneProvider {
    String getTitle();

    Scene getScene();

    default void setActive() {
        SceneManager.getInstance().getStage().setScene(getScene());
        SceneManager.getInstance().getStage().setTitle(getTitle());
    }
}
