package snake.coordination;

import org.jspace.SequentialSpace;
import org.jspace.Space;
import snake.common.Direction;
import snake.common.Point;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.*;
import snake.state.Snake;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;

public class CoordinationLobby implements Runnable {
    private static class PlayerConnection {
        private final PlayerInfo info;
        private boolean ready = false;

        public PlayerConnection(PlayerInfo info) {
            this.info = info;
        }
    }

    public enum LobbyState {
        WAITING, STARTED, FINISHED
    }

    private final String lobbyId;
    private final HashMap<String, PlayerConnection> players = new HashMap<>();
    private final Space space = new SequentialSpace();
    private final CoordinationServer server;
    private LobbyState state = LobbyState.WAITING;

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

    public LobbyState getState() {
        return state;
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

            this.state = LobbyState.STARTED;

            HashMap<String, Point> initialPositions = new HashMap<>();
            HashMap<String, Direction> initialDirections = new HashMap<>();
            Random r = new Random();

            int seed = r.nextInt();
            int width = 50;
            int height = 50;

            for (var playerId : players.keySet()) {
                Point pos;

                initialDirections.put(playerId, Direction.random(r));

                // Ensure at least 2 spaces between snakes
                while (true) {
                    pos = Point.random(r, 10, width, 10, height);

                    Point finalPos = pos;

                    if (initialPositions.values().stream().allMatch(p -> p.distanceTo(finalPos) > 2)) {
                        break;
                    }
                }

                initialPositions.put(playerId, pos);
            }

            /* HashMap of every player id and their map of opponent secrets

                Given players P1, P2

                {
                    P1: {
                        P2: "a"
                    },
                    P2: {
                        P1: "b"
                    }
                }

                Here P1 listens on "a" and P2 listens on "b"
                Then P1 connects to P2 on "b" and P2 connects to P1 on "a"

             */

            HashMap<String, HashMap<String, String>> playerSecretMap = new HashMap<>();


            for (var playerId : players.keySet()) {
                for (var opponentId : players.keySet()) {
                    if (playerId.equals(opponentId)) continue;

                    var playerToOpponentSecret = UUID.randomUUID().toString();

                    if (!playerSecretMap.containsKey(playerId)) {
                        playerSecretMap.put(playerId, new HashMap<>());
                    }

                    playerSecretMap.get(playerId).put(opponentId, playerToOpponentSecret);
                }
            }

            // Send start game
            for (var playerId : players.keySet()) {
                ArrayList<OpponentInfo> opponentInfos = new ArrayList<>();

                for (var opponentId : players.keySet()) {
                    if (playerId.equals(opponentId)) continue;

                    var playerInfo = players.get(opponentId).info;
                    var opponentInfo = new OpponentInfo(playerInfo.baseUri(), initialDirections.get(opponentId), initialPositions.get(opponentId), playerSecretMap.get(playerId).get(opponentId), playerSecretMap.get(opponentId).get(playerId));

                    opponentInfos.add(opponentInfo);
                }

                wrappedSpace.put(new StartGame(playerId, width, height, seed, initialDirections.get(playerId), initialPositions.get(playerId), opponentInfos.toArray(new OpponentInfo[0])));
            }

            // this.server.removeLobby(this.lobbyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}