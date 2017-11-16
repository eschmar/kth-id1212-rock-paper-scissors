package io.eschmann.net.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ReceiverServer implements Runnable {
    private ServerSocket serverSocket;
    public Integer port;

    /**
     * Opens a new socket on a random port and starts a new thread.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            System.out.println("listening on port: " + port);
            new Thread(this).start();
        } catch (Exception e) {
            System.out.println("Could not create serverSocket.");
        }
    }

    /**
     * Start handling messages and finally close socket.
     */
    @Override
    public void run() {
        try {
            System.out.println("Successfully opened socket and running!");
            while (true) {
                // handle messages
                new MessageHandler(serverSocket.accept()).handle();
            }
//            while (true) new PeerClientHandler(serverSocket.accept(), controllerObserver).run();
        } catch (IOException e) {
            // catch.
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
