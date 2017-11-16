package io.eschmann.common;

import java.io.Serializable;

public class Message implements Serializable {
    public static final String TYPE_JOIN = "join";

    public String type;
    public String message;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return type + ": " + message;
    }
}
