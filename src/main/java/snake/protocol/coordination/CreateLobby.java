package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "create_lobby", compact = true)
public record CreateLobby(String playerId) {
}
