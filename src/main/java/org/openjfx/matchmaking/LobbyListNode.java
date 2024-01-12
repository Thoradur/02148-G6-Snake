package org.openjfx.matchmaking;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import org.openjfx.NodeProvider;
import javafx.scene.control.Button;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;


public class LobbyListNode implements NodeProvider {
    private final GridPane lobbyUi = new GridPane();

    public Button lobby1 = new Button("lobby1");
    public Button lobby2 = new Button("lobby2");
    public Button lobby3 = new Button("lobby3");
    String lobbyToJoin = "yoyo";

    ;

    public LobbyListNode() throws URISyntaxException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {

        lobbyUi.add(lobby1,0,0);
        lobbyUi.add(lobby2,0,1);
        lobbyUi.add(lobby3,0,2);

    }

    @Override
    public void setScene(Scene parent) {

    }

    @Override
    public Node getNode() {
        return lobbyUi;
    }


    public void chooseLobby() {
        lobby1.setOnAction(e -> {
            System.out.println("do something lobby1");
            lobbyToJoin = "lobby1";
        });
        lobby2.setOnAction(e -> {
            System.out.println("do something lobby2");
            lobbyToJoin = "lobby2";

        });
        lobby3.setOnAction(e -> {
            System.out.println("do something lobby3");
            lobbyToJoin = "lobby3";

        });
    }

}
