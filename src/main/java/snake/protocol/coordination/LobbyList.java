package snake.protocol.coordination;


import snake.protocol.Message;

import java.util.List;

@Message(name = "lobby_list")
public record LobbyList(String[] lobbies) {
}
