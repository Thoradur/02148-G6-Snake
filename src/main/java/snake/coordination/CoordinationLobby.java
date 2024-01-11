package snake.coordination;

import snake.state.Board;
import snake.state.State;

import java.util.concurrent.atomic.AtomicInteger;

public class CoordinationLobby implements Runnable {
    private final int totalPlayers;
    private AtomicInteger readyPlayers;
    private Board board;

    public CoordinationLobby(int totalPlayers, int width, int height, State state) {
        this.totalPlayers = totalPlayers;
        this.readyPlayers = new AtomicInteger(0);
        this.board = new Board(width, height, state);
    }

    //to check if all players are ready
    public synchronized void playerReady() {
        if (readyPlayers.incrementAndGet() == totalPlayers) {
            notifyAll(); // Notify that all players are ready
        }
    }


    @Override
    public void run() {
        synchronized (this) {
            while (readyPlayers.get() < totalPlayers) {
                // Should there be something like, after X seconds, we start the game, with or without you?
                try {
                    wait(500); // Wait for all players to be ready
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        board.build();
    }
}

