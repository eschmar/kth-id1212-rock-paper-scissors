package io.eschmann.model;

import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.server.ReceiverServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Player {
    public String ip;
    public ReceiverServer server;
    public String username;

    public ArrayList<Opponent> opponents;

    public Player() throws UnknownHostException {
        this.opponents = new ArrayList<Opponent>();

        // Identify local ip address
        InetAddress localAddress = InetAddress.getLocalHost();

        this.ip = localAddress.getHostAddress();
        initServer();

        this.username = server.port.toString();
        System.out.println(this);
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

    @Override
    public String toString() {
        return "P - " + username + " [" + ip + ":" + server.port + "]";
    }
}
