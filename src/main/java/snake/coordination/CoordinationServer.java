package snake.coordination;

import org.jspace.*;

import java.util.HashMap;

public class CoordinationServer implements Runnable {
    SpaceRepository repository = new SpaceRepository();
    HashMap<String, CoordinationLobby> lobbies = new HashMap<>();

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        new CoordinationServer().run();
    }
}
