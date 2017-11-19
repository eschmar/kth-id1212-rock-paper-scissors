package io.eschmann.net.server;

import io.eschmann.common.Message;
import io.eschmann.model.Opponent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;
    protected Opponent playerAsOpponent;
    Observer observer;

    public MessageHandler(Socket socket, Opponent opponent, Observer observer) {
        this.socket = socket;
        this.playerAsOpponent = opponent;
        this.observer = observer;
    }

    public void handle() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // receive message from opponent
            Message message = (Message) in.readObject();

            switch (message.type) {
                case Message.TYPE_JOIN:
                    // respond
                    out.writeObject(playerAsOpponent);
                    observer.addLog("A new player joined.");
                    break;
                default:
                    System.out.println("Unsupported message received...");
            }

            socket.close();
            observer.updateThings();
        } catch (Exception e) {
            System.out.println("MessageHandler error --> " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
