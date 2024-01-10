package snake.coordination;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;
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
    private URI playerURI;
    private URI coordinationServerUri;
    private RemoteSpace coordinationWaitingRoom;
    private RemoteSpace coordinationLobby;

    public CoordinationClient(String playerId, URI playerURI, URI coordinationServerUri) throws IOException {
        this.playerId = playerId;
        this.playerURI = playerURI;
        this.coordinationWaitingRoom = new RemoteSpace(coordinationServerUri);
    }

    public void sendPlayerInfo() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var wrappedSpace = new MessageSpace(coordinationLobby);
        wrappedSpace.put(new PlayerInfo(playerId, playerURI));
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
        return (StartGame) wrappedSpace.get(new StartGame(playerId, null));
    }

    public void createLobby() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var wrappedSpace = new MessageSpace(coordinationWaitingRoom);
        wrappedSpace.put(new CreateLobby());
    }

    public LobbyList listLobbies() throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var wrappedSpace = new MessageSpace(coordinationWaitingRoom);
        wrappedSpace.put(new ListLobbies());
        return (LobbyList) wrappedSpace.get(new LobbyList(null));
    }


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var playerId = UUID.randomUUID().toString();
        var port = new Random().nextInt(65535);
        var playerBaseUri = new URI("tcp://localhost:" + port + "/?keep");

        var c = new CoordinationClient(playerId, playerBaseUri, new URI("tcp://localhost:8111/waiting?keep"));

        c.createLobby();

        var lobbies = c.listLobbies().lobbies();
        var lobbyURI = Arrays.stream(lobbies).toList().getLast();

        System.out.println("Connecting to lobby: " + lobbyURI);

        c.coordinationLobby = new RemoteSpace(lobbyURI);

        c.sendPlayerInfo();
        c.ready();

        var startGame = c.waitForStart();

        System.out.println(Arrays.toString(startGame.players()));
    }
}
