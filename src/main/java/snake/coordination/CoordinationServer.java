package snake.coordination;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jspace.*;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.CreateLobby;
import snake.protocol.coordination.ListLobbies;
import snake.protocol.coordination.LobbyCreated;
import snake.protocol.coordination.LobbyList;

public class CoordinationServer implements Runnable {
    private final URI coordinationServerUri;
    private final SpaceRepository repository = new SpaceRepository();

    // Create a local space for the game lobby
    private final SequentialSpace waitingRoom = new SequentialSpace();

    private HashMap<String, CoordinationLobby> lobbies = new HashMap<>();

    public static void main(String[] args) throws URISyntaxException {
        var s = new CoordinationServer(new URI("tcp://0.0.0.0:8111/?keep"));
        s.run();
    }

    public CoordinationServer(URI coordinationServerUri) {
        this.coordinationServerUri = coordinationServerUri;
    }

    public MessageSpace getWaitingRoom() {
        return new MessageSpace(waitingRoom);
    }

    public void addLobby(String lobbyId, CoordinationLobby lobby) {
        this.repository.add(lobbyId, lobby.getSpace());
        this.lobbies.put(lobbyId, lobby);
    }

    public void removeLobby(String lobbyId) {
        var lobby = this.lobbies.get(lobbyId);

        if (lobby == null) return;

        this.repository.remove(lobbyId);
        this.lobbies.remove(lobbyId);
    }

    public void run() {
        try {
            var wrappedWaitingRoom = new MessageSpace(waitingRoom);
            // Add the space to the repository
            repository.add("waiting", waitingRoom);

            // Set the URI of the game space
            System.out.println("Listening on: " + this.coordinationServerUri);
            repository.addGate(this.coordinationServerUri);

            var messageTemplate = MessageRegistry.getTemplateUnion(ListLobbies.class, CreateLobby.class);

            // Keep serving requests to enter game rooms
            while (true) {
                var nextMessage = MessageRegistry.fromTuple(waitingRoom.get(messageTemplate));

                if (nextMessage instanceof CreateLobby createLobby) {
                    // Create new lobby
                    var lobby = new CoordinationLobby(this);
                    new Thread(lobby).start();
                    wrappedWaitingRoom.put(new LobbyCreated(createLobby.playerId(), lobby.getLobbyId()));
                }

                if (nextMessage instanceof ListLobbies listLobbies) {
                    System.out.println("Listing lobbies");
                    var lobbyIds = lobbies.values().stream()
                            .filter(lobby -> lobby.getState() == CoordinationLobby.LobbyState.WAITING)
                            .map(CoordinationLobby::getLobbyId).toList().toArray(new String[0]);
                    
                    wrappedWaitingRoom.put(new LobbyList(lobbyIds));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}