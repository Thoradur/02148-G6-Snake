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
    private final Snake snake;
    private final OpponentInfo opponentInfo;
    private final OpponentNode opponentNode;
    private final Thread opponentThread;

    public Opponent(OpponentInfo opponentInfo, State state, SpaceRepository repository) throws IOException {
        this.opponentInfo = opponentInfo;
        this.snake = new Snake(List.of(opponentInfo.startSnake()));
        this.opponentNode = new OpponentNode(opponentInfo, state, this.snake);
        this.opponentThread = new Thread(opponentNode);

        repository.add(opponentInfo.playerSecret(), space);
    }

    public Space getSpace() {
        return space;
    }

    public void spawn() {
        opponentThread.start();
    }
}
