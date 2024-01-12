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
    CoordinationClient coordinationClient = new CoordinationClient("hej", new URI(""), new URI("tcp://localhost:8111/waiting?keep"));


    public MatchMakingScene() throws URISyntaxException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        this.scene = new Scene(root);

        LobbyListNode lobbyListNode = new LobbyListNode();
        root.setCenter(lobbyListNode.getNode());
        lobbyListNode.chooseLobby();
        //lobbyToJoin contains the string of the name of the lobby the user clicked
        //This is where we join a lobby - but it does not run I think
        coordinationClient.joinLobby(lobbyListNode.lobbyToJoin);
        coordinationClient.createLobby();
        coordinationClient.listLobbies();

        //root.setCenter(new InLobbyNode().getNode());
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
