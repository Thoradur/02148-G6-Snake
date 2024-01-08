package org.openjfx;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.awt.Point;

public class fruit {
    GraphicsContext gc;
    Scene scene;

    Point position = new Point();

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CELL_SIZE = 25;

    fruit(GraphicsContext gc, Scene scene) {
        this.gc = gc;
        this.scene = scene;
        System.out.println("fruit constructor");
        randomizePosition();
    }


    public void run(GraphicsContext gc) {
        draw(gc);
    }
    
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(position.x * CELL_SIZE, position.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    public void randomizePosition() {
        position.x = (int) (Math.random() * (SCREEN_WIDTH / CELL_SIZE));
        position.y = (int) (Math.random() * (SCREEN_HEIGHT / CELL_SIZE));
    }
}
