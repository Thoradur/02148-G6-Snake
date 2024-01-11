package org.openjfx.startscreen;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.openjfx.NodeProvider;

public class StartScreenNode implements NodeProvider {
    private GridPane root = new GridPane();

    public StartScreenNode() {
        // Add some buttons etc
        root.add(new Text("Hello World"), 0, 0);
        root.add(new Text("Hello World"), 0, 1);
        root.add(new Text("Hello World"), 0, 2);
    }

    @Override
    public void setScene(Scene parent) {

    }

    @Override
    public Node getNode() {
        return root;
    }
}
