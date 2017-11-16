package io.eschmann.model;

import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.server.ReceiverServer;

import java.io.IOException;
import java.util.ArrayList;

public class Player {
    public String ip;
    public ReceiverServer server;
    public String username;

    public ArrayList<Opponent> opponents;

    public Player(String ip) {
        this.ip = ip;
        initServer();
        this.username = server.port.toString();
        this.opponents = new ArrayList<Opponent>();
        System.out.println("New player on " + this.ip + ":" + this.server.port + "!");
    }

    protected void addOpponent(Opponent opponent) {
        if (opponents.contains(opponent)) {
            return;
        }

        System.out.println(opponent);
        opponents.add(opponent);
    }

    /**
     * Start new receiver process
     */
    private void initServer() {
        server = new ReceiverServer(ip);
        server.start();
    }

    public void joinGame(String ip, int port) throws IOException, ClassNotFoundException {
        OpponentConnection conn = new OpponentConnection();
        conn.connect(new Opponent(ip, port));
        Opponent newOpponent = conn.sendJoinMessage();
        addOpponent(newOpponent);
        conn.disconnect();
    }

    public Opponent toOpponent() {
        return new Opponent(ip, server.port, username);
    }
}
