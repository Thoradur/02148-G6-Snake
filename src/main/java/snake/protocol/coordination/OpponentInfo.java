package snake.protocol.coordination;

import snake.common.Direction;
import snake.common.Point;

import java.net.URI;

/**
 * @param baseUri        The base URI of the opponent
 * @param opponentSecret The secret of the opponent
 * @param playerSecret   The secret of the player
 */
public record OpponentInfo(URI baseUri, Direction startDirection, Point[] startSnake, String opponentSecret,
                           String playerSecret) {
}
