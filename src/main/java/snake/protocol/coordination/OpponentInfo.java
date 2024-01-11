package snake.protocol.coordination;

import java.net.URI;

/**
 * @param baseUri        The base URI of the opponent
 * @param opponentSecret The secret of the opponent
 * @param playerSecret   The secret of the player
 */
public record OpponentInfo(URI baseUri, String opponentSecret, String playerSecret) {
}
