package snake.protocol.state;

import snake.common.Direction;
import snake.common.Point;
import snake.protocol.Message;

@Message(name = "fragment", compact = false)
public record StateUpdate(Integer step, Direction direction, Point[] compactSnake) {
}
