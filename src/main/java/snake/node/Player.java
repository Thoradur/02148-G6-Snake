package snake.node;

import javafx.application.Platform;
import org.jspace.SpaceRepository;
import org.openjfx.SceneManager;
import org.openjfx.snake.SnakeScene;
import snake.common.Point;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.StartGame;
import snake.protocol.state.StateUpdate;
import snake.state.Board;
import snake.state.Snake;
import snake.state.State;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
        this.state = new State(startGame.seed());
        this.board = new Board(startGame.width(), startGame.height(), this.state);
        this.snake = new Snake(List.of(startGame.startPosition()));
        this.snake.setDirection(startGame.startDirection());
        this.state.getGameObjects().add(this.snake);

        // Print my snake identityHash
        System.out.println("My snake: " + System.identityHashCode(this.snake));

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

    public void sendStateUpdate(StateUpdate update) {
        // Send to all opponents
        this.opponents.stream().map(Opponent::getOutGoingSpace).forEach(space -> {
            try {
                new MessageSpace(space).put(update);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Sent state update: " + update.step() + " - " + Arrays.toString(update.compactSnake()));
    }

    @Override
    public void run() {
        System.out.println("Listening on " + uri);

        repository.addGate(uri);

        // Start all opponents
        opponents.stream().map(Thread::new).forEach(Thread::start);

        long minimumDelta = 250;
        long lastBuild = System.currentTimeMillis();

        while (true) {

            try {
                // Sleep for 1/60th of a second
                Thread.sleep(1000 / 60);

                // CHeck if all opponents are ready, reduce to a boolean
                var allOpponentsReady = opponents.stream().map(Opponent::isReady).reduce(true, (a, b) -> a && b);

                if (!allOpponentsReady) {

                    if (System.currentTimeMillis() - lastBuild > 1000) {
                        System.out.println("Waiting for opponents to connect for more than 1000");
                    }

                    continue;
                }

                lastBuild = System.currentTimeMillis();

                board.build();

                // Call draw on the canvas
                SceneManager.getInstance().getSceneProvider(SnakeScene.class).getSnakeCanvas().draw();

                // Ensure minimum delta
                long delta = System.currentTimeMillis() - lastBuild;

                if (delta < minimumDelta) {
                    Thread.sleep(minimumDelta - delta);
                }

                // Step all except opponent snakes and build board.
                state.step(gameObject -> {
                    if (gameObject instanceof Snake s) {
                        return s == snake;
                    }

                    return true;
                });

                var stateUpdate = new StateUpdate(state.getStep(), snake.getDirection(), snake.getDehydratedSnake().toArray(new Point[0]));
                sendStateUpdate(stateUpdate);

                if (snake.isDead()) {
                    System.out.println("You died!");
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Exit everything
        Platform.exit();
        System.exit(0);
    }
}
