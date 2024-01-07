package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "joinLobby")
public record JoinLobby(String lobbyName) {
}
