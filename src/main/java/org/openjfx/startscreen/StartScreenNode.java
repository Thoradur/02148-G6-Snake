package org.openjfx.startscreen;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.openjfx.NodeProvider;
import org.openjfx.SystemInfo;
import org.openjfx.matchmaking.MatchMakingScene;

import java.net.URI;
import java.util.UUID;

public class StartScreenNode implements NodeProvider {
    private BorderPane root = new BorderPane();
    private GridPane grid = new GridPane();


    public StartScreenNode() {
        // Add some buttons etc
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);

        var matchMakingButton = new Button("Start matchmaking");

        var myHost = new TextField("localhost");
        var myPort = new TextField(SystemInfo.getClientPort().toString());

        var matchmakingServerURI = new TextField("tcp://localhost:8111");

        matchMakingButton.setOnAction(e -> {
            var playerID = UUID.randomUUID().toString();

            var matchMakingScene = new MatchMakingScene(
                    playerID,
                    URI.create("tcp://" + myHost.getText() + ":" + myPort.getText() + "/?keep"),
                    URI.create(matchmakingServerURI.getText())
            );

            matchMakingScene.setActive();
        });

        grid.add(new Text("Host"), 0, 0);
        grid.add(myHost, 1, 0);
        grid.add(new Text("Port"), 0, 1);
        grid.add(myPort, 1, 1);
        grid.add(new Text("Matchmaking Server URI"), 0, 2);
        grid.add(matchmakingServerURI, 1, 2);
        grid.add(matchMakingButton, 0, 3);

        root.setCenter(grid);
    }

    @Override
    public void setScene(Scene parent) {
    }

    @Override
    public Node getNode() {
        return root;
    }
}
