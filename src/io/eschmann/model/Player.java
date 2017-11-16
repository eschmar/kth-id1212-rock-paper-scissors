package io.eschmann.model;

public class Player {
    public String ip;
    public int port;

    public Player(String ip, int port) {
        this.ip = ip;
        this.port = port;
        System.out.println("New player on " + this.ip + ":" + this.port + "!");
    }
}
