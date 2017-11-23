package io.eschmann.net;

import io.eschmann.net.common.Observer;
import io.eschmann.net.server.MessageHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class CommunicationServer implements Runnable {
    private InetAddress localAddress;
    private ServerSocket serverSocket;
    public Observer observer;

    /**
     * Opens a new socket on a random port.
     */
    public CommunicationServer() {
        try {
            // Identify local ip address
            localAddress = InetAddress.getLocalHost();

            // setup socket
            serverSocket = new ServerSocket(0);
            System.out.println("Listening on port: " + getPort());
        } catch (Exception e) {
            System.out.println("Could not create serverSocket.");
        }
    }

    public String getIpAddress() {
        return localAddress.getHostAddress();
    }

    public Integer getPort() {
        if (serverSocket == null || serverSocket.isClosed()) return null;
        return serverSocket.getLocalPort();
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
                System.out.println("Port " + getPort() + " closed.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not close serverSocket.");
            }
        }
    }
}
