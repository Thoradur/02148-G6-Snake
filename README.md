# P2P Snake Game

Github repository <https://github.com/Thoradur/02148-G6-Snake>

## How to run

Firstly, ensure you have compiled the most recent version of the game, if you are using the maven commands to run the
program.

```bash
mvn clean compiler:compile
```

To play the game as intended you need to run the coordination server.

Its entrypoint can be found in the "snake.coordination.CoordinationServer" main class.

Alternatively it can be run through your preferred IDE or with maven:

```bash
mvn exec:java -Dexec.mainClass="snake.coordination.CoordinationServer"
```

It will start the coordination server on port 8111 listening by default on 0.0.0.0. Which can be confirmed
by looking at the first line of the output.

Once the coordination server is running you can start any N instances of the main game with the following command:

```bash
mvn clean javafx:run
```

By default, all instances are configured to broadcast their local host as `localhost` and they will
select a random port in the upper ranges that is available automatically.

The URI to the coordination server can be configured to point on some centralized host, by default it
points to localhost.

If running the game over a local "external" network between devices, on windows you'll
need to trust the network as "private" and disable all firewalls on the private network.

Once you're ready and the coordination server is running, you can press "Start matchmaking".

Here it will list all non-started lobbies, you can on one of the clients create a new one
by pressing "Create Lobby". A UUID of the newly appeared lobby will pop up.

On the other clients you can refresh the list to see the lobby you'll join.

Press the ID of the lobby to join once, then press "Join Lobby" to join the lobby.

When all players are "ready" the game will start.

> Note, if you are trying to play with more than 2 players, ensure everyone joins before readying up, as the game
> will start as soon as all the players (at least 2) are ready.

The game will now give you a grace period of 2 seconds before trying to connect
to its opponents and the game is off.

> TIP: Full screen the game to see the entire board and prevent going out of bounds.

The game never ends, except when you die.

Use WASD or arrows to move.
