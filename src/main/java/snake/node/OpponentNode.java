package snake.node;

import org.jspace.RemoteSpace;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpaceProxy;
import snake.protocol.state.Fragment;
import snake.state.Snake;
import snake.state.State;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class OpponentNode implements Runnable {
    private URI uri;
    private RemoteSpace space;
    private Snake snake;
    private State state;

    public OpponentNode(URI uri, State state, Snake snake) throws IOException {
        this.uri = uri;
        this.state = state;
        this.snake = snake;
    }

    public RemoteSpace getSpace() {
        return space;
    }

    @Override
    public void run() {
        try {
            // SLeep for 5 seconds
            System.out.println("Sleeping for 5 seconds waiting for server to start");
            Thread.sleep(5000);

            System.out.println("Connecting to " + uri);
            this.space = new RemoteSpace(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var proxy = new MessageSpaceProxy(this.space);
        var template = MessageRegistry.getMessageFactory(Fragment.class).toTemplate();

        while (true) {
            try {
                var stateUpdate = (Fragment) MessageRegistry.fromTuple(space.get(template));

                System.out.println("Received state update: " + stateUpdate);

                for (var gameObject : state.getGameObjects()) {
                    if (gameObject instanceof Snake s && s == this.snake) {
                        s.setDehydratedSnake(Arrays.asList(stateUpdate.compactSnake()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
