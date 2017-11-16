package io.eschmann.net.server;

import io.eschmann.common.MessageWrapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    public void handle() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            MessageWrapper message = (MessageWrapper) in.readObject();
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
