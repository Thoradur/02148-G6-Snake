package snake.protocol.state;

import snake.common.Point;
import snake.protocol.Message;

@Message(name = "fragment", compact = false)
public record StateUpdate(Integer step, Point[] compactSnake) {
}
