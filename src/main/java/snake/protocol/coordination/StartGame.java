package snake.protocol.coordination;

import snake.protocol.Message;

import java.net.URI;

@Message(name = "start_game", compact = false)
public record StartGame(String playerId, URI[] players) {
}
