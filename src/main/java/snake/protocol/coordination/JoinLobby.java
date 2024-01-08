package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "joinLobby", compact = true)
public record JoinLobby(String lobbyName) {
}
