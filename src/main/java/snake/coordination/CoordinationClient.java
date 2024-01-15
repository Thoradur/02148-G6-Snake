package snake.coordination;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;
import org.openjfx.startscreen.StartScreenNode;
import snake.protocol.MessageSpace;
import snake.protocol.coordination.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class CoordinationClient {
    private String playerId;
    private String playerName;
    private URI playerURI;
    private URI coordinationServerUri;
    private RemoteSpace coordinationWaitingRoom;
    private RemoteSpace coordinationLobby;
    static StartScreenNode startScreenNode = new StartScreenNode();

    public CoordinationClient(String playerId, URI playerURI, URI coordinationServerUri, String playerName) throws IOException {
        this.playerId = playerId;
        this.playerURI = playerURI;
        this.coordinationServerUri = coordinationServerUri;
        this.coordinationWaitingRoom = new RemoteSpace(coordinationServerUri);
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public URI getPlayerURI() {
        return playerURI;
    }

    public String getPlayerName(){ return playerName; }

    public void sendPlayerInfo() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var wrappedSpace = new MessageSpace(coordinationLobby);
        wrappedSpace.put(new PlayerInfo(playerId, playerURI, playerName));
    }

    public void ready() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var wrappedSpace = new MessageSpace(coordinationLobby);
        wrappedSpace.put(new Ready(playerId));
    }

    public void leave() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var wrappedSpace = new MessageSpace(coordinationLobby);
        wrappedSpace.put(new LeaveLobby(playerId));
    }


    public StartGame waitForStart() throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var wrappedSpace = new MessageSpace(coordinationLobby);
        return (StartGame) wrappedSpace.get(new StartGame(playerId, null, null, null, null, null, null));
    }

    public LobbyCreated createLobby() throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var wrappedSpace = new MessageSpace(coordinationWaitingRoom);
        wrappedSpace.put(new CreateLobby(playerId));
        return (LobbyCreated) wrappedSpace.get(new LobbyCreated(playerId, null));
    }

    public LobbyList listLobbies() throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var wrappedSpace = new MessageSpace(coordinationWaitingRoom);
        wrappedSpace.put(new ListLobbies());
        return (LobbyList) wrappedSpace.get(new LobbyList(null));
    }

    //for joining a new lobby - connecting to a uri that contains the lobbyId
    public void joinLobby(String lobbyId) throws URISyntaxException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException {
        //lobbyUri should be changed to something else - this is temporary
        URI lobbyUri = new URI("tcp://" + coordinationServerUri.getHost() + ":" + coordinationServerUri.getPort() + "/" + lobbyId + "?keep");
        //System.out.println("Trying to join: " + lobbyUri);
        this.coordinationLobby = new RemoteSpace(lobbyUri);
        this.sendPlayerInfo();

        System.out.println("joined: " + lobbyUri);
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var playerId = UUID.randomUUID().toString();
        var port = new Random().nextInt(65535);
        var playerBaseUri = new URI("tcp://localhost:" + port + "/?keep");
        var playerName = startScreenNode.playerName.getText();

        var c = new CoordinationClient(playerId, playerBaseUri, new URI("tcp://localhost:8111/waiting?keep"), playerName);

        String lobbyId;


        var lobbies = c.listLobbies().lobbies();

        System.out.println(Arrays.toString(lobbies));

        if (lobbies.length > 0) {
            lobbyId = lobbies[0];
        } else {
            lobbyId = c.createLobby().lobbyId();
        }


        URI lobbyUri = new URI("tcp://localhost:8111/" + lobbyId + "?keep");

        System.out.println("Connecting to lobby: " + lobbyUri);

        c.coordinationLobby = new RemoteSpace(lobbyUri);

        c.sendPlayerInfo();
        c.ready();

        var startGame = c.waitForStart();

        System.out.println(Arrays.toString(startGame.opponents()));
    }
}
