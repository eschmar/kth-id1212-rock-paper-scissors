package io.eschmann.model;

import io.eschmann.controller.GameController;
import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.server.ReceiverServer;

import java.io.IOException;
import java.net.InetAddress;
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

    public Player(String ip, Integer port) throws UnknownHostException {
        this.opponents = new ArrayList<Opponent>();
        this.opponentMoves = new HashMap<Integer, ArrayList<String>>();
        this.moves = new HashMap<Integer, String>();
        this.ip = ip;
        this.port = port;
        this.username = port.toString();
    }

    public void makeMove(String move) {
        if (!move.equals(Game.MOVE_ROCK) && !move.equals(Game.MOVE_PAPER) && !move.equals(Game.MOVE_SCISSORS)) {
            throw new IllegalArgumentException("Illegal move detected!");
        }

        System.out.println("Made move " + move);
        moves.put(roundCount, move);

        for (Opponent opponent : opponents) {
            CompletableFuture.runAsync(() -> {
                try {
                    OpponentConnection opponentConnection = new OpponentConnection();
                    opponentConnection.connect(opponent);
                    opponentConnection.sendMove(this.toOpponent(), move, roundCount);
                    opponentConnection.disconnect();
                } catch (Exception e) {
                    System.out.println("Unable to send move.");
                }
            });
        }

        updateScore();
    }

    public void addOpponent(Opponent opponent) {
        System.out.println("addOpponent " + opponent);
        if (opponents.contains(opponent) || (ip.equals(opponent.ip) && port.equals(opponent.port))) {
            return;
        }

        opponents.add(opponent);
        CompletableFuture.runAsync(() -> {
            try {
                OpponentConnection opponentConnection = new OpponentConnection();
                opponentConnection.connect(opponent);
                opponentConnection.announceMyself(this.toOpponent());
                opponentConnection.disconnect();
            } catch (Exception e) {
                System.out.println("Unable to join game.");
            }
        });
    }

    public void addOpponents(ArrayList<Opponent> opponents) {
        for (Opponent opponent : opponents) {
            addOpponent(opponent);
        }
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

    public void addOpponentMove(Opponent opponent, String move, int round) {
        if (!opponents.contains(opponent)) {
            // only allow known opponents to participate.
            return;
        }

        if (!opponentMoves.containsKey(round)) {
            opponentMoves.put(round, new ArrayList<String>());
        }

        opponentMoves.get(round).add(move);
        updateScore();
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

        System.out.println("Update score ");
        if (this.hasReceivedAllAnswers() && this.moves.containsKey(this.roundCount)) {
            this.roundCount++;
        }
    }

    public boolean hasReceivedAllAnswers() {
        System.out.println("round: " + roundCount);
//        System.out.println("1 hasReceivedAllAnswers " + this.opponents.size() + " / " + this.getNumberOfPlays(roundCount - 1));
//        if (roundCount < 1) return false;
//        System.out.println("2 hasReceivedAllAnswers " + this.opponents.size() + " / " + this.getNumberOfPlays(roundCount - 1));
        return this.opponents.size() == getNumberOfPlays(roundCount - 1);
    }

    public int getNumberOfPlays(int turn) {
        if (!this.opponentMoves.containsKey(turn)) return 0;
        return this.opponentMoves.get(turn).size();
    }
}
