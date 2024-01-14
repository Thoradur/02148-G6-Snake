package org.openjfx;

import javafx.stage.Stage;

import java.util.HashMap;

public class SceneManager {
    private final static HashMap<Class<?>, SceneProvider> scenes = new HashMap<>();
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

    public void registerSceneProvider(Class<?> clazz, SceneProvider sceneProvider) {
        scenes.put(clazz, sceneProvider);
    }

    public void setActive(Class<?> clazz) {
        var sceneProvider = scenes.get(clazz);

        if (sceneProvider == null) {
            throw new RuntimeException("Scene not found");
        }

        this.stage.setScene(sceneProvider.getScene());
        this.stage.setTitle(sceneProvider.getTitle());
    }

    public <T extends SceneProvider> T getSceneProvider(Class<T> clazz) {
        var sceneProvider = scenes.get(clazz);

        if (sceneProvider == null) {
            return null;
        }

        return (T) sceneProvider;
    }
}
