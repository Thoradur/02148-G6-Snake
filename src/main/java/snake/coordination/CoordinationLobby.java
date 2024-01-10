package snake.coordination;

import org.jspace.SequentialSpace;
import org.jspace.Space;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.LeaveLobby;
import snake.protocol.coordination.PlayerInfo;
import snake.protocol.coordination.Ready;
import snake.protocol.coordination.StartGame;

import java.util.HashMap;
import java.util.UUID;

public class CoordinationLobby implements Runnable {
    private static class PlayerConnection {
        private final PlayerInfo info;
        private boolean ready = false;

        public PlayerConnection(PlayerInfo info) {
            this.info = info;
        }
    }

    private final String lobbyId;
    private final HashMap<String, PlayerConnection> players = new HashMap<>();
    private final Space space = new SequentialSpace();
    private final CoordinationServer server;


    public CoordinationLobby(CoordinationServer server) {
        // Generate uuid lobby id
        this.lobbyId = UUID.randomUUID().toString();
        this.server = server;
        this.server.addLobby(this.lobbyId, this);
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public Space getSpace() {
        return space;
    }

    @Override
    public void run() {
        try {
            var wrappedSpace = new MessageSpace(this.space);
            var messageTemplate = MessageRegistry.getTemplateUnion(PlayerInfo.class, LeaveLobby.class, Ready.class);

            mainLoop:
            while (true) {
                var nextMessage = MessageRegistry.fromTuple(this.space.get(messageTemplate));

                if (nextMessage instanceof PlayerInfo playerInfo) {
                    if (players.containsKey(playerInfo.playerId())) continue;

                    players.put(playerInfo.playerId(), new PlayerConnection(playerInfo));

                }

                if (nextMessage instanceof LeaveLobby leaveLobby) {
                    players.remove(leaveLobby.playerId());
                }

                if (nextMessage instanceof Ready ready) {
                    var playerConnection = players.get(ready.playerId());

                    if (playerConnection == null) continue;

                    playerConnection.ready = true;
                }


                for (var playerConnection : players.values()) {
                    if (!playerConnection.ready) continue mainLoop;
                }


                if (players.size() >= 2) break;
            }

            // Send start game
            for (var playerId : players.keySet()) {
                wrappedSpace.put(new StartGame(playerId, new String[0]));
            }

            this.server.removeLobby(this.lobbyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}