package org.openjfx;


import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.*;


import snake.common.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.*;
import javafx.util.Duration;
import snake.common.Direction;
import snake.state.Board;
import snake.state.Snake;
import snake.state.State;


public class SnakeGame {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CELL_SIZE = 25;
    private static final int START_LENGTH = 3;

    private GraphicsContext gc;
    private Board board;
    private State state;
    private Snake snake;

    public SnakeGame(State state, Board board, Snake snake) {
        this.board = board;
        this.snake = snake;
        this.state = state;
    }

    public void start(Stage stage) {
/*        state = new State();
        board = new Board(SCREEN_WIDTH / CELL_SIZE, SCREEN_HEIGHT / CELL_SIZE, state);
        snake = new Snake(new Point(SCREEN_WIDTH / 2 / CELL_SIZE, SCREEN_HEIGHT / 2 / CELL_SIZE), Direction.random(new Random()));

        //adds stuff to board
        state.getGameObjects().add(snake);*/

        //crates window
        stage.setTitle("SnakeGame");
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gc = canvas.getGraphicsContext2D();

        //KEYBOARD INPUT
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case W:
                        if (snake.getDirection() != Direction.DOWN) {
                            snake.setDirection(Direction.UP);
                        }
                        System.out.println("w");
                        break;
                    case A:
                        if (snake.getDirection() != Direction.RIGHT) {
                            snake.setDirection(Direction.LEFT);
                        }
                        System.out.println("A");
                        break;
                    case S:
                        if (snake.getDirection() != Direction.UP) {
                            snake.setDirection(Direction.DOWN);
                        }
                        System.out.println("S");
                        break;
                    case D:
                        if (snake.getDirection() != Direction.LEFT) {
                            snake.setDirection(Direction.RIGHT);
                        }
                        System.out.println("D");
                        break;
                }
            }
        });

        snake.grow(START_LENGTH);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        // Draws background and snake on screen
        drawBackground(gc);

        state.getGameObjects().forEach(gameObject -> {
            if (gameObject instanceof Snake s) {
                drawSnake(gc, s);
            }
        });

        // Updates the snake and board
        board.build();
    }

    public void drawBackground(GraphicsContext gc) {
        // Draws background
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


    private void drawSnake(GraphicsContext gc, Snake snake) {
        //draws snake
        gc.setFill(Color.web("00FF00"));
        gc.fillRect(snake.getHead().x() * CELL_SIZE, snake.getHead().y() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);

        var snakeBody = snake.getSnake();
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).x() * CELL_SIZE, snakeBody.get(i).y() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1, 20, 20);
        }

    }


}
