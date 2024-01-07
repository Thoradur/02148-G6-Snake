package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "leaveLobby")
public record LeaveLobby() {
}
