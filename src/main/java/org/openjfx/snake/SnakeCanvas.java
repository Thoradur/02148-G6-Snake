package org.openjfx.snake;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
//import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.openjfx.NodeProvider;
import snake.node.Player;
import snake.state.*;

import java.nio.IntBuffer;
import java.util.Arrays;


public class SnakeCanvas implements NodeProvider {
    private final Canvas canvas;
    private final State state;
    private final Board board;
    private final Player player;
    public static final int CELL_SIZE = 10;
    private final int W;
    private final int H;
    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();

    private final int[] backgroundBuffer;

    public SnakeCanvas(Player player) {
        this.state = player.getState();
        this.board = state.getBoard();
        W = board.getWidth() * CELL_SIZE;
        H = board.getHeight() * CELL_SIZE;
        this.canvas = new Canvas(W, H);
        this.player = player;
        this.backgroundBuffer = new int[W * H];


        var c1 = Color.web("bdc4b7");
        var c2 = Color.web("cbd1c5");

        for (int i = 0; i < W; i+=CELL_SIZE) {
            for (int j = 0; j < H; j+= CELL_SIZE) {
                int ci = (i/CELL_SIZE + j/CELL_SIZE) % 2 == 0 ? toInt(c1) : toInt(c2);
                for (int dx = 0; dx < CELL_SIZE; dx++) {
                    for (int dy = 0; dy < CELL_SIZE; dy++) {
                        backgroundBuffer[i + dx + W * (j + dy)] = ci;
                    }
                }
            }
        }



    }

    private int toInt(Color c) {
        return
                (255 << 24) |
                        ((int) (c.getRed() * 255) << 16) |
                        ((int) (c.getGreen() * 255) << 8) |
                        ((int) (c.getBlue() * 255));
    }


    public void draw() {
        long startTime = System.nanoTime();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter p = gc.getPixelWriter();

        // Draws background
        p.setPixels(0, 0, W, H, pixelFormat, backgroundBuffer, 0, W);
        System.out.println("Drawing background took " + (System.nanoTime() - startTime) / 1000000 + "ms");

        state.getGameObjects().forEach(gameObject -> {
            if (gameObject instanceof Snake s) {
                drawSnake(gc, s);
            }
            if (gameObject instanceof Fruit f) {
                drawFruit(gc, f);
            }
        });
        System.out.println("Drawing background took after snakes and fruit " + (System.nanoTime() - startTime) / 1000000 + "ms");
    }

    public void drawSnake(GraphicsContext gc, Snake snake) {
        
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
        gc.fillRect(snake.getHead().x() * CELL_SIZE, snake.getHead().y() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);

        var snakeBody = snake.getSnake();
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).x() * CELL_SIZE, snakeBody.get(i).y() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1, 20, 20);
        }
    }

    public void drawFruit(GraphicsContext gc, Fruit fruit) {

        gc.setFill(Color.web("FF0000"));
        gc.fillRect(fruit.getPosition().x() * CELL_SIZE, fruit.getPosition().y() * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
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
