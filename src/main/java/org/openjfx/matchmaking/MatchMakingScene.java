package org.openjfx.matchmaking;

import javafx.scene.Scene;
import org.openjfx.SceneProvider;

public class MatchMakingScene implements SceneProvider {
    private final Scene scene;

    public MatchMakingScene() {
        this.scene = new Scene(null);
    }

    @Override
    public String getTitle() {
        return "Match Making";
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
