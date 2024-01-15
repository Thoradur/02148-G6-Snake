package org.openjfx.startscreen;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.openjfx.SceneProvider;
import org.openjfx.scoreboard.ScoreboardNode;
import snake.coordination.CoordinationClient;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.LeaveLobby;
import snake.protocol.coordination.PlayerInfo;
import snake.protocol.coordination.Ready;

public class StartScreenScene implements SceneProvider {
    private final BorderPane root = new BorderPane();
    private final Scene scene;

    public StartScreenScene() {
        var startScreenNode = new StartScreenNode();


        this.scene = new Scene(root);
        startScreenNode.setScene(this.scene);

        root.setCenter(startScreenNode.getNode());
        root.setPrefWidth(800);
        root.setPrefHeight(600);
    }

    @Override
    public String getTitle() {
        return "Start Screen";
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
