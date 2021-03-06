package io.eschmann.net.client;

import io.eschmann.net.common.Message;
import io.eschmann.model.Opponent;
import io.eschmann.model.Player;
import io.eschmann.net.common.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

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

    public void sendJoinMessage(Player player, Observer gameObserver) throws IOException, ClassNotFoundException {
        Message msg = new Message(Message.TYPE_JOIN, player.toOpponent(), null);
        out.writeObject(msg);

        Message message = (Message) in.readObject();
        Opponent newOpponent = message.opponent;
        gameObserver.opponentWantsToJoin(newOpponent, message.opponents);
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

    public void announceMyself(Opponent opponent) throws IOException {
        Message msg = new Message(Message.TYPE_ANNOUNCE, opponent);
        out.writeObject(msg);
    }

    public void sendMove(Opponent opponent, String move, Integer round) throws IOException {
        Message msg = new Message(Message.TYPE_MOVE, opponent, move, round);
        out.writeObject(msg);
    }
}
