package io.eschmann.model;

import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.server.ReceiverServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Player {
    public String ip;
    public Integer port;
    public String username;

    private ArrayList<Opponent> opponents;
    private int roundCount = 0;
    private int score = 0;
    private int losses = 0;

    public Player(String ip, Integer port) throws UnknownHostException {
        this.opponents = new ArrayList<Opponent>();
        this.ip = ip;
        this.port = port;
        this.username = port.toString();
    }

    public void addOpponent(Opponent opponent) {
        if (opponents.contains(opponent)) {
            return;
        }

        opponents.add(opponent);
    }

    public void addOpponents(ArrayList<Opponent> opponents) {
        for (Opponent opponent : opponents) {
            addOpponent(opponent);
        }
    }

    public Opponent toOpponent() {
        return new Opponent(ip, port, username);
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
}
