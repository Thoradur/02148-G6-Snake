package snake.coordination;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.UnknownHostException;

public class CoordinationLobby implements Runnable{

    private String uri;
    private RemoteSpace lobby;
    public CoordinationLobby(String uri) throws IOException {
        this.uri = uri;
    }
    public void run() {

        try {

            // Connect to the remote lobby
            System.out.println("Connecting to lobby " + uri + "...");
            this.lobby = new RemoteSpace(uri);

            // Read gamertag from the console
            System.out.print("Enter your gamertag: ");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String name = input.readLine();

            // Read game-room from the console
            System.out.print("Select a game room: ");
            String gameroom = input.readLine();
            // Send request to enter chatroom
            lobby.put("enter",name,gameroom);

            // Get response with chatroom URI
            Object[] response = lobby.get(new ActualField("roomURI"), new ActualField(name), new ActualField(gameroom), new FormalField(String.class));
            String gameroom_uri = (String) response[3];
            System.out.println("Connecting to lobby space " + gameroom_uri);
            RemoteSpace gameroom_space = new RemoteSpace(gameroom_uri);

            // Keeping to make sure there is a connection
            System.out.println("Start gaming...");

            while(true) {
                String message = input.readLine();
                gameroom_space.put(name, message);
            }


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


/*
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class CoordinationLobby {

    public static void main(String[] args) {

        try {

            // Set the URI of the loby of the game server
            String uri = "tcp://127.0.0.1:9001/lobby?keep";

            // Connect to the remote lobby
            System.out.println("Connecting to lobby " + uri + "...");
            RemoteSpace lobby = new RemoteSpace(uri);

            // Read gamertag from the console
            System.out.print("Enter your gamertag: ");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String name = input.readLine();

            // Read game-room from the console
            System.out.print("Select a game room: ");
            String gameroom = input.readLine();

            // Send request to enter chatroom
            lobby.put("enter",name,gameroom);

            // Get response with chatroom URI
            Object[] response = lobby.get(new ActualField("roomURI"), new ActualField(name), new ActualField(gameroom), new FormalField(String.class));
            String gameroom_uri = (String) response[3];
            System.out.println("Connecting to chat space " + gameroom_uri);
            RemoteSpace chatroom_space = new RemoteSpace(gameroom_uri);

            // Keeping to make sure there is a connection
            System.out.println("Start gaming...");
            while(true) {
                String message = input.readLine();
                chatroom_space.put(name, message);
            }


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

 */