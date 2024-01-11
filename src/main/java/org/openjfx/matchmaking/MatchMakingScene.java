package org.openjfx.matchmaking;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.openjfx.SceneProvider;
import snake.coordination.CoordinationClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MatchMakingScene implements SceneProvider {
    private final Scene scene;
    private final BorderPane root = new BorderPane();

    public MatchMakingScene() throws URISyntaxException, IOException {
        this.scene = new Scene(root);

        var coordinationClient = new CoordinationClient("hej", new URI(""), new URI(""));

        root.setCenter(new LobbyListNode().getNode());

        root.setCenter(new InLobbyNode().getNode());
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
