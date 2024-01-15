package snake.node;

import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;
import snake.protocol.coordination.OpponentInfo;
import snake.state.Snake;
import snake.state.State;

import java.io.IOException;
import java.util.List;

public class Opponent {
    private final Space space = new SequentialSpace();
    private final State state;
    private final Snake snake;
    private final OpponentInfo opponentInfo;
    private final OpponentNode opponentNode;
    private final Thread opponentThread;

    private final String playerNames;

    public Opponent(OpponentInfo opponentInfo, State state, SpaceRepository repository, String playerNames) throws IOException {
        this.opponentInfo = opponentInfo;
        this.state = state;
        this.snake = new Snake(List.of(opponentInfo.startPosition()));
        this.playerNames = playerNames;
        this.snake.setDirection(opponentInfo.startDirection());
        this.state.getGameObjects().add(this.snake);

        this.opponentNode = new OpponentNode(opponentInfo, this.state, this.snake);
        this.opponentThread = new Thread(opponentNode);

        repository.add(opponentInfo.playerSecret(), space);
    }

    public OpponentInfo getOpponentInfo() {
        return opponentInfo;
    }

    public boolean isReady() {
        return opponentNode.isReady();
    }

    public Space getSpace() {
        return space;
    }

    public void spawn() {
        opponentThread.start();
    }
}
