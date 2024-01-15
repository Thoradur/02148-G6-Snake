package snake.protocol.coordination;

import snake.protocol.Message;

import java.net.URI;

@Message(name = "player_info", compact = true)
public record PlayerInfo(String playerId, URI baseUri, String playerName) {

}
