package snake.node;

import org.jspace.RemoteSpace;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.OpponentInfo;
import snake.protocol.state.StateUpdate;
import snake.state.Snake;
import snake.state.State;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class OpponentNode implements Runnable {
    private OpponentInfo info;
    private RemoteSpace space;
    private Snake snake;
    private State state;

    private int step = 0;

    public OpponentNode(OpponentInfo info, State state, Snake snake) throws IOException {
        this.info = info;
        this.state = state;
        this.snake = snake;
    }

    public RemoteSpace getSpace() {
        return space;
    }

    public int getStep() {
        return step;
    }

    public boolean isReady() {
        return this.space != null && this.step == this.state.getStep() || this.snake.isDead();
    }

    @Override
    public void run() {
        try {
            var uri = new URI("tcp://" + info.baseUri().getHost() + ":" + info.baseUri().getPort() + "/" + info.opponentSecret() + "?keep");

            System.out.println("Sleeping for 2 seconds waiting for server to start");
            Thread.sleep(2000);

            System.out.println("Connecting to " + uri);

            this.space = new RemoteSpace(uri);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var messageSpace = new MessageSpace(this.space);

        while (!snake.isDead()) {
            try {
                // Get next state update
                var stateUpdate = (StateUpdate) messageSpace.get(new StateUpdate(this.step + 1, null, null));
                this.step++;

                System.out.println("Received state update: " + stateUpdate.step() + " - " + Arrays.toString(stateUpdate.compactSnake()));

                var prevHead = this.snake.getHead();

                this.snake.setDehydratedSnake(Arrays.asList(stateUpdate.compactSnake()));
                this.snake.setDirection(stateUpdate.direction());

                if (prevHead.distanceTo(this.snake.getHead()) > 1) {
                    System.out.println("prev: " + prevHead + " new head: " + this.snake.getHead());
                    System.out.println("Snake moved more than one step, killing.");

                    this.snake.kill();
                }


            } catch (Exception e) {
                System.out.println("Got error of type: " + e.getMessage());
            }
        }

        System.out.println("Opponent is dead, exiting");
    }
}
