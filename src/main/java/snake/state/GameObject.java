package snake.state;

public interface GameObject {
    int getStep();

    void step();

    void build(Board board);
}
