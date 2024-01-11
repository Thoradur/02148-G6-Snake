package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "ready", compact = true)
public record Ready(String playerId) {
}
