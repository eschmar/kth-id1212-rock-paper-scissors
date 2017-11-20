package io.eschmann.model;

public class Game {
    public static final String MOVE_ROCK = "rock";
    public static final String MOVE_PAPER = "paper";
    public static final String MOVE_SCISSORS = "scissors";

    public static boolean play(String move, String opponentMove) {
        if (move.equals(MOVE_ROCK) && opponentMove.equals(MOVE_SCISSORS)) return true;
        if (move.equals(MOVE_PAPER) && opponentMove.equals(MOVE_ROCK)) return true;
        if (move.equals(MOVE_SCISSORS) && opponentMove.equals(MOVE_PAPER)) return true;
        return false;
    }
}
