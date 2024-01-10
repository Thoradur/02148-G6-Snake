package snake.coordination;

import java.util.HashMap;
import java.util.Map;
import org.jspace.*;

public class CoordinationServer implements Runnable{

    private SpaceRepository repository;
    private SequentialSpace lobby;
    public void run() {
        try {

            // Create a repository
            this.repository = new SpaceRepository();

            // Create a local space for the game lobby
            this.lobby = new SequentialSpace();

            // Add the space to the repository
            repository.add("lobby",lobby);

            // Set the URI of the game space
            String uri = "tcp://127.0.0.8:9001/lobby?keep";

            // Open a gate
            repository.addGate("tcp://127.0.0.8:9001/?keep");
            System.out.println("Opening repository gate at " + uri + "...");

            // This space is where we have the game rooms
            SequentialSpace rooms = new SequentialSpace();

            // Keep serving requests to enter game rooms
            while (true) {

                // roomN will be used to ensure every chat space has a unique name
                Integer roomC = 0;

                String roomURI;

                while (true) {
                    // Read request
                    Object[] request = lobby.get(new ActualField("enter"),new FormalField(String.class), new FormalField(String.class));
                    String who = (String) request[1];
                    String roomID = (String) request[2];
                    System.out.println(who + " requesting to enter " + roomID + "...");

                    // If room exists just prepare the response with the corresponding URI
                    Object[] the_room = rooms.queryp(new ActualField(roomID),new FormalField(Integer.class));
                    if (the_room != null) {
                        roomURI = "tcp://127.0.0.8:9001/lobby" + the_room[1] + "?keep";
                    }
                    // If the room does not exist, create the room and launch a room handler
                    else {
                        System.out.println("Creating room " + roomID + " for " + who + " ...");
                        roomURI = "tcp://127.0.0.8:9001/lobby" + roomC + "?keep";
                        System.out.println("Setting up game lobby space " + roomURI + "...");
                        new Thread(new roomHandler(roomID,"lobby"+roomC,roomURI,repository)).start();
                        rooms.put(roomID,roomC);
                        roomC++;
                    }

                    // Sending response back to the game client
                    System.out.println("Telling " + who + " to go for room " + roomID + " at " + roomURI + "...");
                    lobby.put("roomURI", who, roomID, roomURI);
                }


            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class roomHandler implements Runnable {

    private Space game;
    private String roomID;
    private String spaceID;

    public roomHandler(String roomID, String spaceID, String uri, SpaceRepository repository) {

        this.roomID = roomID;
        this.spaceID = spaceID;

        // Create a local space for the chatroom
        game = new SequentialSpace();

        // Add the space to the repository
        repository.add(this.spaceID, game);

    }

    @Override
    public void run() {
        try {

            // Printing messages written in the lobby to see the connection
            while (true) {
                Object[] message = game.get(new FormalField(String.class), new FormalField(String.class));
                System.out.println("ROOM " + roomID + " | " + message[0] + ":" + message[1]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

/*
import java.util.HashMap;
import java.util.Map;
import org.jspace.*;

public class CoordinationServer {

    public static void main(String[] args) {
        try {

            // Create a repository
            SpaceRepository repository = new SpaceRepository();

            // Create a local space for the chat messages
            SequentialSpace lobby = new SequentialSpace();

            // Add the space to the repository
            repository.add("lobby",lobby);

            // Set the URI of the game space
            String uri = "tcp://127.0.0.1:9001/lobby?keep";

            // Open a gate
            repository.addGate("tcp://127.0.0.1:9001/?keep");
            System.out.println("Opening repository gate at " + uri + "...");

            // This space is where we have the game rooms
            SequentialSpace rooms = new SequentialSpace();

            // Keep serving requests to enter game rooms
            while (true) {

                // roomN will be used to ensure every chat space has a unique name
                Integer roomC = 0;

                String roomURI;

                while (true) {
                    // Read request
                    Object[] request = lobby.get(new ActualField("enter"),new FormalField(String.class), new FormalField(String.class));
                    String who = (String) request[1];
                    String roomID = (String) request[2];
                    System.out.println(who + " requesting to enter " + roomID + "...");

                    // If room exists just prepare the response with the corresponding URI
                    Object[] the_room = rooms.queryp(new ActualField(roomID),new FormalField(Integer.class));
                    if (the_room != null) {
                        roomURI = "tcp://127.0.0.1:9001/chat" + the_room[1] + "?keep";
                    }
                    // If the room does not exist, create the room and launch a room handler
                    else {
                        System.out.println("Creating room " + roomID + " for " + who + " ...");
                        roomURI = "tcp://127.0.0.1:9001/chat" + roomC + "?keep";
                        System.out.println("Setting up chat space " + roomURI + "...");
                        new Thread(new roomHandler(roomID,"chat"+roomC,roomURI,repository)).start();
                        rooms.put(roomID,roomC);
                        roomC++;
                    }

                    // Sending response back to the game client
                    System.out.println("Telling " + who + " to go for room " + roomID + " at " + roomURI + "...");
                    lobby.put("roomURI", who, roomID, roomURI);
                }


            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class roomHandler implements Runnable {

    private Space game;
    private String roomID;
    private String spaceID;

    public roomHandler(String roomID, String spaceID, String uri, SpaceRepository repository) {

        this.roomID = roomID;
        this.spaceID = spaceID;

        // Create a local space for the chatroom
        game = new SequentialSpace();

        // Add the space to the repository
        repository.add(this.spaceID, game);

    }

    @Override
    public void run() {
        try {

            // Printing messages written in the lobby to see the connection
            while (true) {
                Object[] message = game.get(new FormalField(String.class), new FormalField(String.class));
                System.out.println("ROOM " + roomID + " | " + message[0] + ":" + message[1]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
*/
