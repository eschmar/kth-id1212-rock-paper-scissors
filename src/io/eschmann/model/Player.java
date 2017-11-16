package io.eschmann.model;

import io.eschmann.net.server.ReceiverServer;

import java.util.ArrayList;

public class Player {
    public String ip;
    public ReceiverServer server;
    public String username;

    public ArrayList<String> opponents;

    public Player(String ip) {
        this.ip = ip;
        initServer();
        this.opponents = new ArrayList<String>();
        System.out.println("New player on " + this.ip + ":" + this.server.port + "!");
    }

    /**
     * Start new receiver process
     */
    private void initServer() {
        server = new ReceiverServer();
        server.start();
    }
}
