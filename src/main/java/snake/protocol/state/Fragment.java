package snake.protocol.state;

import snake.common.Point;
import snake.protocol.Message;

@Message(name = "fragment", compact = false)
public record Fragment(Integer step, Point[] compactSnake, Boolean ack) {
}
