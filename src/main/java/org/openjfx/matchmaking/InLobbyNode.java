package org.openjfx.matchmaking;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.openjfx.NodeProvider;
import org.openjfx.snake.SnakeScene;
import snake.coordination.CoordinationClient;
import snake.node.Player;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.LeaveLobby;
import snake.protocol.coordination.PlayerInfo;
import snake.protocol.coordination.Ready;

public class InLobbyNode implements NodeProvider {
    private final CoordinationClient coordinationClient;
    private final BorderPane root = new BorderPane();

    public InLobbyNode(CoordinationClient coordinationClient) {
        this.coordinationClient = coordinationClient;

        var readyButton = new Button("Ready");
        var leaveButton = new Button("Leave");


        readyButton.setOnAction(e -> {
            try {
                coordinationClient.ready();
                var startGame = coordinationClient.waitForStart();

                var player = new Player(coordinationClient.getPlayerURI(), startGame, coordinationClient.getPlayerName());
                var gameScene = new SnakeScene(player);
                gameScene.setActive();
                new Thread(player).start();

            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        leaveButton.setOnAction(e -> {
            try {
                coordinationClient.leave();

                // Exit the lobby TODO
                System.exit(0);

            } catch (Exception err) {
                err.printStackTrace();
            }
        });

        var box = new VBox(readyButton, leaveButton);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
    }

    @Override
    public void setScene(Scene parent) {

    }

    @Override
    public Node getNode() {
        return root;
    }
}
