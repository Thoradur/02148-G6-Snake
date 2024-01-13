package org.openjfx.matchmaking;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.openjfx.NodeProvider;
import javafx.scene.control.Button;
import snake.coordination.CoordinationClient;

import java.util.ArrayList;
import java.util.List;


public class LobbyListNode implements NodeProvider {
    private final BorderPane lobbyUi = new BorderPane();
    private final GridPane topLobbyButtons = new GridPane();
    private final GridPane lobbyButtons = new GridPane();
    private final GridPane lobbyList = new GridPane();

    private final List<String> lobbies = new ArrayList<>();
    private String lobbyToJoin;
    private final CoordinationClient coordinationClient;

    // Callback function when joined lobby
    private final Runnable joinedLobbyCallback;

    public LobbyListNode(CoordinationClient coordinationClient, Runnable joinedLobbyCallback) {
        this.coordinationClient = coordinationClient;
        this.joinedLobbyCallback = joinedLobbyCallback;

        var createLobbyButton = new Button("Create Lobby");
        var refreshLobbyButton = new Button("Refresh Lobby List");

        var joinLobbyButton = new Button("Join Lobby");

        createLobbyButton.setOnAction(e -> {
            try {
                coordinationClient.createLobby();
            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        refreshLobbyButton.setOnAction(e -> {
            try {
                var lobbies = coordinationClient.listLobbies();
                this.lobbies.clear();
                this.lobbies.addAll(List.of(lobbies.lobbies()));
                this.fillLobbyList();
            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        joinLobbyButton.setOnAction(e -> {
            try {
                coordinationClient.joinLobby(lobbyToJoin);
                joinedLobbyCallback.run();
            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        topLobbyButtons.setAlignment(Pos.CENTER);
        topLobbyButtons.setHgap(10);
        topLobbyButtons.add(createLobbyButton, 0, 0);
        topLobbyButtons.add(refreshLobbyButton, 1, 0);

        lobbyButtons.setAlignment(Pos.CENTER);
        lobbyButtons.setHgap(10);
        lobbyButtons.add(joinLobbyButton, 0, 0);

        lobbyList.setAlignment(Pos.CENTER);
        lobbyList.setHgap(10);

        lobbyUi.setTop(topLobbyButtons);
        lobbyUi.setCenter(lobbyList);
        lobbyUi.setBottom(lobbyButtons);

        this.fillLobbyList();
    }

    private void fillLobbyList() {
        lobbyList.getChildren().clear();

        for (int i = 0; i < lobbies.size(); i++) {
            Button lobby = new Button(lobbies.get(i));
            int finalI = i;

            lobby.setOnAction(e -> {
                lobbyToJoin = lobbies.get(finalI);
            });

            lobbyList.add(lobby, 0, i);
        }
    }

    @Override
    public void setScene(Scene parent) {

    }

    public String getLobbyToJoin() {
        return lobbyToJoin;
    }

    @Override
    public Node getNode() {
        return lobbyUi;
    }
}
