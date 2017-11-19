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

    public ArrayList<Opponent> opponents;
    public int roundCount = 0;
    public int score = 0;
    public int losses = 0;

    public Player(String ip, Integer port) throws UnknownHostException {
        this.opponents = new ArrayList<Opponent>();
        this.ip = ip;
        this.port = port;
        this.username = port.toString();

        System.out.println(this);
    }

    public void addOpponent(Opponent opponent) {
        if (opponents.contains(opponent)) {
            return;
        }

        System.out.println(opponent);
        opponents.add(opponent);
    }

    public Opponent toOpponent() {
        return new Opponent(ip, port, username);
    }

    @Override
    public String toString() {
        return "P - " + username + " [" + ip + ":" + port + "]";
    }
}
