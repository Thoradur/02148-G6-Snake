package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "lobbyCreated", compact = false)
public record LobbyCreated(String playerId, String lobbyId) {
}
