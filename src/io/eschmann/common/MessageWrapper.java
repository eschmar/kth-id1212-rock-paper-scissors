package io.eschmann.common;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    public String message;

    public MessageWrapper(String message) {
        this.message = message;
    }
}
