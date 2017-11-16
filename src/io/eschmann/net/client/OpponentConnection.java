package io.eschmann.net.client;

import io.eschmann.common.Message;
import io.eschmann.model.Opponent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class OpponentConnection {
    private static final int TIMEOUT_READ_BLOCK = 1800000;
    private static final int TIMEOUT_CONNECTION = 30000;

    private boolean connected = false;

    private Socket opponentSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Connect to a specific opponent.
     * @param opponent
     * @throws IOException
     */
    public void connect(Opponent opponent) throws IOException {
        opponentSocket = new Socket();
        opponentSocket.connect(new InetSocketAddress(opponent.ip, opponent.port), TIMEOUT_CONNECTION);
        opponentSocket.setSoTimeout(TIMEOUT_READ_BLOCK);
        connected = true;

        out = new ObjectOutputStream(opponentSocket.getOutputStream());
        in = new ObjectInputStream(opponentSocket.getInputStream());

//        new Thread(new Listener(broadcastHandler)).start();
    }

    public Opponent sendJoinMessage() throws IOException, ClassNotFoundException {
        out.writeObject(new Message(Message.TYPE_JOIN, "Pew."));
        return (Opponent) in.readObject();
    }

    /**
     * Close connection with opponent.
     * @throws IOException
     */
    public void disconnect() throws IOException {
        in.close();
        out.close();
        opponentSocket.close();
    }
}
