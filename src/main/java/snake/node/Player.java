package snake.node;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import snake.common.Point;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.StartGame;
import snake.protocol.state.StateUpdate;
import snake.state.Board;
import snake.state.Fruit;
import snake.state.GameObject;
import snake.state.Snake;
import snake.state.State;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private URI uri;
    private State state;
    private Board board;
    private Snake snake;

    //i created a var for the random seed, probs need to be else where, but its here
    private int seed = 10;

    private SpaceRepository repository = new SpaceRepository();
    private final List<Opponent> opponents = new ArrayList<>();
    private final Fruit[] fruits = new Fruit[10];

    public Player(URI uri, StartGame startGame) {
        this.uri = uri;
        this.state = new State(startGame.seed());
        this.board = new Board(startGame.width(), startGame.height(), this.state);
        this.snake = new Snake(List.of(startGame.startSnake()));
        this.snake.setDirection(startGame.startDirection());
        this.state.getGameObjects().add(this.snake);

        for (var opponentInfo : startGame.opponents()) {
            try {
                var opponent = new Opponent(opponentInfo, state, repository);
                opponents.add(opponent);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public State getState() {
        return state;
    }

    public Board getBoard() {
        return board;
    }

    public Snake getSnake() {
        return snake;
    }

    public void putFragment(StateUpdate fragment) {
        // Send to all opponents
        this.opponents.stream().map(Opponent::getSpace).forEach(space -> {
            try {
                new MessageSpace(space).put(fragment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Sent fragment: " + fragment);
    }

    @Override
    public void run() {
        System.out.println("Listening on " + uri);

        var opponentSpace = new SequentialSpace();

        repository.add("opponent" + uri.getPort(), opponentSpace);
        repository.addGate(uri);

        // Start all opponents
        opponents.forEach(Opponent::spawn);

        for (int i = 0; i < fruits.length; i++) {
            // fruits[i] = new Fruit(board, seed);
            // state.getGameObjects().add(fruits[i]);
        }

        for (int i = 0; i < fruits.length; i++) {
            // fruits[i] = new Fruit(board, seed);
            state.getGameObjects().add(fruits[i]);
        }


        while (true) {
            try {
                Thread.sleep(1000);

                // Step all and build board.
                state.step();
                board.build();

                var fragment = new StateUpdate(state.getStep(), snake.getDehydratedSnake().toArray(new Point[0]));
                putFragment(fragment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
