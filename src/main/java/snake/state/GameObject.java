package snake.state;

import snake.protocol.state.Fragment;

public interface GameObject {
    int getStep();

    Fragment step();

    void build(Board board);
}
