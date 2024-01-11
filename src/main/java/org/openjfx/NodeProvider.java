package org.openjfx;

import javafx.scene.Node;
import javafx.scene.Scene;

public interface NodeProvider {
    void setScene(Scene parent);

    Node getNode();
}
