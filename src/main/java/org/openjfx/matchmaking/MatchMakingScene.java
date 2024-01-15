package org.openjfx.matchmaking;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.openjfx.SceneProvider;
import snake.coordination.CoordinationClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

public class MatchMakingScene implements SceneProvider {
    private final Scene scene;
    private final BorderPane root = new BorderPane();
    private CoordinationClient coordinationClient;
    private LobbyListNode lobbyListNode;
    private InLobbyNode inLobbyNode;
    private String playerName;

    public MatchMakingScene(String playerId, URI clientURI, URI serverURI, String playerName) {
        this.scene = new Scene(root);

        try {
            this.coordinationClient = new CoordinationClient(playerId, clientURI, URI.create("tcp://" + serverURI.getHost() + ":" + serverURI.getPort() + "/waiting?keep"), playerName);
            this.inLobbyNode = new InLobbyNode(this.coordinationClient);

            this.lobbyListNode = new LobbyListNode(this.coordinationClient, () -> {
                this.root.setCenter(this.inLobbyNode.getNode());
            });

            this.root.setCenter(this.lobbyListNode.getNode());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
