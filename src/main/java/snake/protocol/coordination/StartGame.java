package snake.protocol.coordination;

import snake.common.Point;
import snake.protocol.Message;

import java.net.URI;

@Message(name = "start_game", compact = false)
public record StartGame(
        String playerId,
        Integer width,
        Integer height,
        Integer seed,
        Point[] startSnake,
        OpponentInfo[] opponents
) {
}
