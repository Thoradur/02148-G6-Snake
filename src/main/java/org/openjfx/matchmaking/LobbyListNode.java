package org.openjfx.matchmaking;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import org.openjfx.NodeProvider;

public class LobbyListNode implements NodeProvider {
    private final GridPane lobbyUi = new GridPane();
    ;

    public LobbyListNode() {

    }

    @Override
    public void setScene(Scene parent) {

    }

    @Override
    public Node getNode() {
        return lobbyUi;
    }
}
