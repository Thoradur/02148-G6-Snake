package snake.state;

import snake.protocol.state.StateUpdate;

public interface GameObject {
    void step();

    void build(Board board);
}
