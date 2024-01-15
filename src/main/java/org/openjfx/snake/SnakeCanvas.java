package org.openjfx.snake;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.openjfx.NodeProvider;
import snake.node.Player;
import snake.state.Board;
import snake.state.Snake;
import snake.state.State;
import snake.state.Fruit;

public class SnakeCanvas implements NodeProvider {
    private final Canvas canvas;
    private final State state;
    private final Board board;
    private final Player player;
    public static final int CELL_SIZE = 10;

    public SnakeCanvas(Player player) {
        this.state = player.getState();
        this.board = state.getBoard();
        this.canvas = new Canvas(this.board.getWidth() * CELL_SIZE, this.board.getHeight() * CELL_SIZE);
        this.player = player;
    }

    public int getCellSize() {
        // return (int) Math.min(canvas.getHeight(), canvas.getWidth()) / board.getWidth();
        return CELL_SIZE;
    }

    public void draw() {
        long startTime = System.nanoTime();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int cellSize = getCellSize();

        // Draws background
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("cbd1c5"));
                } else {
                    gc.setFill(Color.web("bdc4b7"));
                }
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
        // System.out.println("Drawing background took " + (System.nanoTime() - startTime) / 1000000 + "ms");

        state.getGameObjects().forEach(gameObject -> {
            if (gameObject instanceof Snake s) {
                drawSnake(gc, s, cellSize);
            }
            if (gameObject instanceof Fruit f) {
                drawFruit(gc, f, cellSize);
            }
        });
        // System.out.println("Drawing background took after snakes and fruit " + (System.nanoTime() - startTime) / 1000000 + "ms");
    }

    public void drawSnake(GraphicsContext gc, Snake snake, int cellSize) {
        // int cellSize = getCellSize();

        //draws snake
        if (!snake.isDead()) {
            if (player.getSnake() == snake) {
                gc.setFill(Color.web("00FF00"));
            } else {
                gc.setFill(Color.web("0000FF"));
            }
        } else {
            gc.setFill(Color.web("FF0000"));
        }
        gc.fillRect(snake.getHead().x() * cellSize, snake.getHead().y() * cellSize, cellSize - 1, cellSize - 1);

        var snakeBody = snake.getSnake();
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).x() * cellSize, snakeBody.get(i).y() * cellSize, cellSize - 1,
                    cellSize - 1, 20, 20);
        }
    }

    public void drawFruit(GraphicsContext gc, Fruit fruit, int cellSize) {
        // int cellSize = getCellSize();
        System.out.println(cellSize);
        gc.setFill(Color.web("FF69B4"));
        gc.fillRect(fruit.getPosition().x() * cellSize, fruit.getPosition().y() * cellSize, cellSize - 1, cellSize - 1);
    }

    @Override
    public void setScene(Scene parent) {
        // canvas.widthProperty().bind(parent.widthProperty());
        // canvas.heightProperty().bind(parent.heightProperty());
    }

    @Override
    public Node getNode() {
        return canvas;
    }
}
