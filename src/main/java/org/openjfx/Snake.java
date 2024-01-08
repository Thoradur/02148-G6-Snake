package org.openjfx;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Snake {
    GraphicsContext gc;
    Scene scene;
    private int currentDirection;

    List<Point> snakeBody = new ArrayList<>();
    Point snakeHead;
    private static final int startLength = 3;

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CELL_SIZE = 25;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    Snake(GraphicsContext gc, Scene scene) {
        this.gc = gc;
        this.scene = scene;
        System.out.println("Snake constructor");

        // KEYBOARD INPUT
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case W:
                        if (currentDirection != DOWN) {
                            currentDirection = UP;
                        }
                        System.out.println("w");
                        break;
                    case A:
                        if (currentDirection != RIGHT) {
                            currentDirection = LEFT;
                        }
                        System.out.println("A");
                        break;
                    case S:
                        if (currentDirection != UP) {
                            currentDirection = DOWN;
                        }
                        System.out.println("S");
                        break;
                    case D:
                        if (currentDirection != LEFT) {
                            currentDirection = RIGHT;
                        }
                        System.out.println("D");
                        break;
                }
            }
        });

        for (int i = 0; i < startLength; i++) {
            snakeBody.add(new Point(SCREEN_WIDTH / 2 / CELL_SIZE, SCREEN_HEIGHT / 2 / CELL_SIZE + i));
        }
        snakeHead = snakeBody.get(0);
    }

    void grow() {
        snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1).x, snakeBody.get(snakeBody.size() - 1).y));
    }

    void run(GraphicsContext gc) {
        drawSnake(gc);
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }

    }

    private void drawSnake(GraphicsContext gc) {
        // draws snake
        gc.setFill(Color.web("008000"));
        gc.fillRect(snakeHead.getX() * CELL_SIZE, snakeHead.getY() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * CELL_SIZE, snakeBody.get(i).getY() * CELL_SIZE, CELL_SIZE - 1,
                    CELL_SIZE - 1, 20, 20);
        }

    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }
}
