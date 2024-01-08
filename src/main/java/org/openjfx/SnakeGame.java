package org.openjfx;



import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.*;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.util.Duration;



class SnakeGame{
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CELL_SIZE = 25;

    public static Snake[] snake = new Snake[1];
    public static fruit[] fruit = new fruit[5];

    public static 

    private GraphicsContext gc;

    public void start(Stage stage) {

        stage.setTitle("SnakeGame");
        Group root = new Group(); //HOLDS BUTTON
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gc = canvas.getGraphicsContext2D();
        Snake snake1 = new Snake(gc, scene);
        snake[0] = snake1;

        for (int i = 0; i < fruit.length; i++) {
            fruit[i] = new fruit(gc, scene);

        }


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void drawBackground(GraphicsContext gc) {
        // draws background
        for (int i = 0; i < SCREEN_WIDTH / CELL_SIZE; i++) {
            for (int j = 0; j < SCREEN_HEIGHT / CELL_SIZE; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("cbd1c5"));
                } else {
                    gc.setFill(Color.web("bdc4b7"));
                }
                gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void collision() {
        for(int j = 0; j < fruit.length; j++){
                if (snake[i].snakeHead.x == fruit[j].position.x && snake[i].snakeHead.y == fruit[j].position.y) {
                    fruit[j].randomizePosition();
                    snake[i].grow();
                }
            }
    }

    private void run(GraphicsContext gc) {
        drawBackground(gc);
        for (int i = 0; i < fruit.length; i++) {
            fruit[i].run(gc);
        }
        
        for(int i = 0; i  < 1 /*snake.length*/; i++){
            snake[i].run(gc);
        }
        
    }
   




}
