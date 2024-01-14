package org.openjfx;

import javafx.scene.Scene;

public interface SceneProvider {
    String getTitle();

    Scene getScene();

    default void setActive() {
        var instance = SceneManager.getInstance();

        if (instance.getSceneProvider(this.getClass()) != this) {
            instance.registerSceneProvider(this.getClass(), this);
        }

        instance.setActive(this.getClass());
    }
}
