package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "leaveLobby", compact = true)
public record LeaveLobby(String playerId) {
}
