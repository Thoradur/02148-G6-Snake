package snake.protocol.coordination;

import snake.protocol.Message;

@Message(name = "listLobbies", compact = true)
public record ListLobbies() {
}
