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
        Message msg = new Message(Message.TYPE_JOIN, "May i join?");
        msg.opponent = player.toOpponent();

        out.writeObject(msg);

        Message message = (Message) in.readObject();
        Opponent newOpponent = message.opponent;
        gameObserver.newPlayerJoined(newOpponent, message.opponents);
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
        Message msg = new Message(Message.TYPE_ANNOUNCE, "I'm new here.");
        msg.opponent = opponent;

        out.writeObject(msg);
    }

    public void sendMove(Opponent opponent, String move, Integer round) throws IOException {
        Message msg = new Message(Message.TYPE_MOVE, "Here's my move.");
        msg.opponent = opponent;
        msg.move = move;
        msg.round = round;

        out.writeObject(msg);
    }
}
