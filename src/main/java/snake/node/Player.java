package snake.node;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import snake.common.Point;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.OpponentInfo;
import snake.protocol.coordination.StartGame;
import snake.protocol.state.Fragment;
import snake.state.Board;
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
    private SpaceRepository repository = new SpaceRepository();
    private final List<Opponent> opponents = new ArrayList<>();

    public Player(URI uri, StartGame startGame) {
        this.uri = uri;
        this.state = new State();
        this.board = new Board(startGame.width(), startGame.height(), this.state);
        this.snake = new Snake(List.of(startGame.startSnake()));

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

    public void putFragment(Fragment fragment) {
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

        while (true) {
            try {
                Thread.sleep(250);

                // Step all and build board.
                state.getGameObjects().forEach(GameObject::step);
                board.build();

                var fragment = new Fragment(snake.getStep(), snake.getDehydratedSnake().toArray(new Point[0]), true);
                putFragment(fragment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
