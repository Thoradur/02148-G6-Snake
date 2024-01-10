package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "start_game", compact = true)
public record StartGame(String playerId, String[] players) {
}
