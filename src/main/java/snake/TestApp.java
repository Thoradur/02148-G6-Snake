package snake;

import snake.common.Direction;
import snake.common.Point;
import snake.protocol.Message;
import snake.state.Board;
import snake.state.GameObject;
import snake.state.Snake;
import snake.state.State;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class TestApp {
    @Message(name = "testMessage")
    public static record TestMessage() {
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
        State state = new State();
        Board board = new Board(10, 10, state);
        Random random = new Random();

        Snake snake = new Snake(Point.random(random, board.getWidth(), board.getHeight()), Direction.random(random));
        Snake snake2 = new Snake(Point.random(random, board.getWidth(), board.getHeight()), Direction.random(random));

        state.getGameObjects().add(snake);


        while (true) {


            board.build();
            state.getGameObjects().forEach(GameObject::step);
            // CLear
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println(board);

            Thread.sleep(1000);
        }
    }
}
