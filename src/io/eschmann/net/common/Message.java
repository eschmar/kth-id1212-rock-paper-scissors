package io.eschmann.net.common;

import io.eschmann.model.Opponent;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public static final String TYPE_JOIN = "join";
    public static final String TYPE_ANNOUNCE = "announce";

    public String type;
    public String message;
    public Opponent opponent;
    public ArrayList<Opponent> opponents;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
        this.opponents = new ArrayList<>();
    }

    @Override
    public String toString() {
        return type + ": " + message;
    }
}
