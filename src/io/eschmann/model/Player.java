package io.eschmann.model;

import io.eschmann.net.client.OpponentConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Player {
    public String ip;
    public Integer port;
    public String username;

    private HashMap<Integer, String> moves;
    private HashMap<Integer, ArrayList<String>> opponentMoves;
    private ArrayList<Opponent> opponents;
    private int roundCount = 0;
    private int score = 0;
    private int losses = 0;

    public boolean canStartNewRound = false;

    public Player(String ip, Integer port) throws UnknownHostException {
        this.opponents = new ArrayList<Opponent>();
        this.opponentMoves = new HashMap<Integer, ArrayList<String>>();
        this.moves = new HashMap<Integer, String>();
        this.ip = ip;
        this.port = port;
        this.username = port.toString();
    }

    public boolean addOpponent(Opponent opponent) {
        if (opponents.contains(opponent) || (ip.equals(opponent.ip) && port.equals(opponent.port))) {
            return false;
        }

        opponents.add(opponent);
        CompletableFuture.runAsync(() -> {
            try {
                OpponentConnection opponentConnection = new OpponentConnection();
                opponentConnection.connect(opponent);
                opponentConnection.announceMyself(this.toOpponent());
                opponentConnection.disconnect();
            } catch (Exception e) {
                System.out.println("Announce failed.");
            }
        });

        return true;
    }

    public ArrayList<Opponent> addOpponents(ArrayList<Opponent> opponents) {
        ArrayList<Opponent> added = new ArrayList<Opponent>();
        for (Opponent opponent : opponents) {
            if (addOpponent(opponent)) {
                added.add(opponent);
            }
        }

        return added;
    }

    public Opponent toOpponent() {
        return new Opponent(ip, port, username);
    }

    public HashMap<Integer, String> getMoves() {
        return moves;
    }

    public ArrayList<Opponent> getOpponents() {
        return opponents;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public int getScore() {
        return score;
    }

    public int getLosses() {
        return losses;
    }

    @Override
    public String toString() {
        return "P - " + username + " [" + ip + ":" + port + "]";
    }

    public void makeMove(String move) {
        if (!move.equals(Game.MOVE_ROCK) && !move.equals(Game.MOVE_PAPER) && !move.equals(Game.MOVE_SCISSORS)) {
            throw new IllegalArgumentException("Illegal move detected!");
        }

        canStartNewRound = false;
        moves.put(roundCount, move);
        int round = roundCount;

        for (Opponent opponent : opponents) {
            CompletableFuture.runAsync(() -> {
                try {
                    OpponentConnection opponentConnection = new OpponentConnection();
                    opponentConnection.connect(opponent);
                    opponentConnection.sendMove(this.toOpponent(), move, round);
                    opponentConnection.disconnect();
                } catch (Exception e) {
                    System.out.println("Unable to send move.");
                }
            });
        }

        updateScore();
    }

    public boolean addOpponentMove(Opponent opponent, String move, int round) {
        if (!opponents.contains(opponent)) {
            // only allow known opponents to participate.
            return false;
        }

        if (!opponentMoves.containsKey(round)) {
            opponentMoves.put(round, new ArrayList<String>());
        }

        opponentMoves.get(round).add(move);
        updateScore();
        return true;
    }

    protected void updateScore() {
        int score = 0;
        int losses = 0;

        for (int i = 0; i <= this.roundCount; i++) {
            if (!this.moves.containsKey(i) || !this.opponentMoves.containsKey(i)) continue;

            for (String opponentMove : this.opponentMoves.get(i)) {
                if (Game.play(this.moves.get(i), opponentMove)) {
                    score++;
                } else {
                    losses++;
                }
            }
        }

        this.score = score;
        this.losses = losses;

        // check whether we can start a new round
        if (!this.moves.containsKey(this.roundCount)) return;
        if (getNumberOfPlays(this.roundCount) == 0) return;
        if (this.opponents.size() != getNumberOfPlays(this.roundCount)) return;

        this.roundCount++;
        canStartNewRound = true;
    }

    public int getNumberOfPlays(int turn) {
        if (!this.opponentMoves.containsKey(turn)) return 0;
        return this.opponentMoves.get(turn).size();
    }
}
