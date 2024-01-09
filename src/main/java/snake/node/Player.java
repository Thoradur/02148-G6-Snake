package snake.node;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import snake.protocol.MessageSpaceProxy;
import snake.state.Snake;
import snake.state.State;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private URI uri;
    private State state;
    private Snake snake;
    private SpaceRepository repository = new SpaceRepository();
    private List<OpponentNode> opponents = new ArrayList<>();

    public Player(URI uri, State state, Snake snake) {
        this.uri = uri;
        this.state = state;
        this.snake = snake;
    }

    @Override
    public void run() {
        repository.addGate(uri);
        System.out.println("Listening on " + uri);

        var opponentSpace = new SequentialSpace();

        repository.add("opponent", opponentSpace);

        while (true) {
            state.getGameObjects().forEach(gameObject -> {
                var fragment = gameObject.step();
                try {
                    new MessageSpaceProxy(opponentSpace).put(fragment);
                    System.out.println("Sent fragment: " + fragment);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
