package org.openjfx.snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.openjfx.NodeProvider;
import snake.state.Board;
import snake.state.Snake;
import snake.state.State;

public class SnakeCanvas implements NodeProvider {
    private final Timeline timeline;
    private final Canvas canvas;
    private final State state;
    private final Board board;

    public SnakeCanvas(State state) {
        this.state = state;
        this.board = state.getBoard();
        this.canvas = new Canvas(0, 0);
        this.timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> draw()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public int getCellSize() {
        return (int) Math.min(canvas.getHeight(), canvas.getWidth()) / board.getWidth();
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int cellSize = getCellSize();

        // Draws background
        for (int i = 0; i < canvas.getWidth() / cellSize; i++) {
            for (int j = 0; j < canvas.getHeight() / cellSize; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("cbd1c5"));
                } else {
                    gc.setFill(Color.web("bdc4b7"));
                }
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }

        state.getGameObjects().forEach(gameObject -> {
            if (gameObject instanceof Snake s) {
                drawSnake(gc, s);
            }
        });
    }

    public void drawSnake(GraphicsContext gc, Snake snake) {
        int cellSize = getCellSize();

        //draws snake
        gc.setFill(Color.web("00FF00"));
        gc.fillRect(snake.getHead().x() * cellSize, snake.getHead().y() * cellSize, cellSize - 1, cellSize - 1);

        var snakeBody = snake.getSnake();
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).x() * cellSize, snakeBody.get(i).y() * cellSize, cellSize - 1, cellSize - 1, 20, 20);
        }
    }

    @Override
    public void setScene(Scene parent) {
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
    }

    @Override
    public Node getNode() {
        return canvas;
    }
}
