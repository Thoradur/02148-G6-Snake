package snake.node;

import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.OpponentInfo;
import snake.protocol.state.StateUpdate;
import snake.state.Snake;
import snake.state.State;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class Opponent implements Runnable {
    private final Space outgoingSpace = new SequentialSpace();
    private final State state;
    private final Snake snake;
    private final OpponentInfo opponentInfo;
    private int step = -1;

    public Opponent(OpponentInfo opponentInfo, State state, SpaceRepository repository) throws IOException {
        this.opponentInfo = opponentInfo;
        this.state = state;
        this.snake = new Snake(List.of(opponentInfo.startPosition()));
        this.snake.setDirection(opponentInfo.startDirection());
        this.state.getGameObjects().add(this.snake);

        repository.add(opponentInfo.playerSecret(), outgoingSpace);
    }

    public int getStep() {
        return step;
    }

    public boolean isReady() {
        return this.step == this.state.getStep() || this.snake.isDead();
    }

    public Space getOutGoingSpace() {
        return outgoingSpace;
    }

    @Override
    public void run() {
        RemoteSpace incomingSpace;

        try {
            var uri = new URI("tcp://" + opponentInfo.baseUri().getHost() + ":" + opponentInfo.baseUri().getPort() + "/" + opponentInfo.opponentSecret() + "?keep");

            System.out.println("Sleeping for 2 seconds waiting for server to start");
            Thread.sleep(2000);

            System.out.println("Connecting to " + uri);

            incomingSpace = new RemoteSpace(uri);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Ready
        this.step = 0;

        var messageSpace = new MessageSpace(incomingSpace);

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
                e.printStackTrace();
                System.out.println("Got error of type: " + e.getMessage());
            }
        }

        System.out.println("Opponent is dead, exiting");
    }
}
