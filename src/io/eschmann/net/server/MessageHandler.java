package io.eschmann.net.server;

import io.eschmann.common.Message;
import io.eschmann.model.Opponent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;
    protected Opponent playerAsOpponent;

    public MessageHandler(Socket socket, Opponent opponent) {
        this.socket = socket;
        this.playerAsOpponent = opponent;
    }

    public void handle() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Message message = (Message) in.readObject();
            switch (message.type) {
                case Message.TYPE_JOIN:
                    out.writeObject(playerAsOpponent);
                    break;
                default:
                    System.out.println("Unsupported message received...");
            }

            System.out.println(message);

//            switch (message.message) {
//                case JOIN:
//                    controllerObserver.addPeer(message.getSenderPeerInfo());
//                    out.writeObject(new MessageWrapper(Message.SYNC, controllerObserver.getPeerInfo()));
//                    break;
//                case LEAVE:
//                    controllerObserver.removePeer(message.getSenderPeerInfo());
//                    break;
//                case MOVE:
//                    controllerObserver.setPeerMove(message.getMove(), message.getSenderPeerInfo());
//                    break;
//                default:
//                    LOGGER.log(Level.SEVERE, "Unrecognized command!");
//            }
            // do things
        } catch (Exception e) {

        }
    }
}
