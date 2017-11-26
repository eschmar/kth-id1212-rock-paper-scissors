package io.eschmann.net;

import io.eschmann.model.Opponent;

import java.io.Serializable;
import java.util.ArrayList;

import io.eschmann.model.Opponent;

import java.io.Serializable;
import java.util.ArrayList;

public class NioMessage implements Serializable {
    public static final String TYPE_JOIN = "join";
    public static final String TYPE_ANNOUNCE = "announce";
    public static final String TYPE_MOVE = "move";
    public static final String DELIMITER = "###";

    public String type;
    public Opponent opponent;
    public ArrayList<Opponent> opponents;
    public String move;
    public int round;

    /**
     * TYPE_JOIN
     * @param type
     * @param opponent
     * @param opponents
     */
    public NioMessage(String type, Opponent opponent, ArrayList<Opponent> opponents) {
        if (!type.equals(TYPE_JOIN)) throw new IllegalArgumentException("Invalid type.");
        this.type = type;
        this.opponent = opponent;
        this.opponents = opponents;

        if (this.opponents == null) this.opponents = new ArrayList<Opponent>();
    }

    /**
     * TYPE_ANNOUNCE
     * @param type
     * @param opponent
     */
    public NioMessage(String type, Opponent opponent) {
        if (!type.equals(TYPE_ANNOUNCE)) throw new IllegalArgumentException("Invalid type.");
        this.type = type;
        this.opponent = opponent;
    }

    /**
     * TYPE_MOVE
     * @param type
     * @param opponent
     * @param move
     * @param round
     */
    public NioMessage(String type, Opponent opponent, String move, int round) {
        if (!type.equals(TYPE_MOVE)) throw new IllegalArgumentException("Invalid type.");
        this.type = type;
        this.opponent = opponent;
        this.move = move;
        this.round = round;
    }

    @Override
    public String toString() {
        return type + DELIMITER + opponent.ip + ":" + opponent.port;
    }
}
