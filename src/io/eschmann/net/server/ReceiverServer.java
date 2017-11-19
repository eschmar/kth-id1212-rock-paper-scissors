package io.eschmann.net.server;

import io.eschmann.model.Opponent;
import io.eschmann.net.common.Observer;

import java.io.IOException;
import java.net.ServerSocket;

public class ReceiverServer implements Runnable {
    private ServerSocket serverSocket;
    public Integer port;
    public String ip;
    public Observer observer;

    /**
     * Opens a new socket on a random port.
     * @param ip
     */
    public ReceiverServer(String ip) {
        this.ip = ip;

        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            System.out.println("listening on port: " + port);
        } catch (Exception e) {
            System.out.println("Could not create serverSocket.");
        }
    }

    /**
     *  Starts a new thread accepting socket connections.
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * Start handling messages and finally close socket.
     */
    @Override
    public void run() {
        try {
            while (true) {
                // handle messages
                new MessageHandler(serverSocket.accept(), observer).handle();
            }
        } catch (IOException e) {
            System.out.println("ReceiverServer experienced an error and will terminate! " + e.getMessage());
        } finally {
            terminate();
        }
    }

    /**
     * Tries to terminate the server.
     */
    public void terminate() {
        if (serverSocket instanceof ServerSocket) {
            try {
                serverSocket.close();
                System.out.println("Port " + port + " closed.");
                port = null;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not close serverSocket.");
            }
        }
    }
}
