package io.eschmann.controller;

import io.eschmann.model.Game;
import io.eschmann.model.Opponent;
import io.eschmann.model.Player;
import io.eschmann.net.CommunicationServer;
import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.common.Observer;
import io.eschmann.net.server.ReceiverServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class GameController {
    protected Player player;
    protected Stage primaryStage;
    protected CommunicationServer server;
    private final OpponentConnection opponentConnection = new OpponentConnection();

    @FXML
    public Label usernameLabel;

    @FXML
    public GridPane mainGrid;

    @FXML
    public TextField myIpText;

    @FXML
    public Label scoreWinLabel;

    @FXML
    public Label scoreLossLabel;

    @FXML
    public Label roundNumberLabel;

    @FXML
    public Label playerCountLabel;

    @FXML
    public Button rockBtn;

    @FXML
    public Button paperBtn;

    @FXML
    public Button scissorsBtn;

    @FXML
    public TextArea logTextarea;

    /**
     * Updates the view with player information and makes the player and stage available to the controller.
     * @param player
     * @param stage
     */
    public void init(Player player, Stage stage, CommunicationServer server, Opponent opponent) {
        this.player = player;
        this.primaryStage = stage;
        this.server = server;

        // start receiver server
        server.observer = new GameObserver();
        server.start();

        // update view with ip and username
        myIpText.setText(player.ip + ":" + player.port);
        usernameLabel.setText(player.username);

        if (opponent == null) return;

        server.addNewNodeChannel(opponent);

//        CompletableFuture.runAsync(() -> {
//            try {
//                opponentConnection.connect(opponent);
//                opponentConnection.sendJoinMessage(player, new GameObserver());
//                opponentConnection.disconnect();
//            } catch (Exception e) {
//                System.out.println("Unable to join game.");
//            }
//        });
    }

    public void onMoveRockAction(ActionEvent actionEvent) {
        makeMove(Game.MOVE_ROCK);
    }

    public void onMovePaperAction(ActionEvent actionEvent) {
        makeMove(Game.MOVE_PAPER);
    }

    public void onMoveScissorsAction(ActionEvent actionEvent) {
        makeMove(Game.MOVE_SCISSORS);
    }

    public void makeMove(String move) {
        rockBtn.setDisable(true);
        paperBtn.setDisable(true);
        scissorsBtn.setDisable(true);

        player.makeMove(move);
        server.observer.updateScoreView();
    }

    private class GameObserver implements Observer {
        @Override
        public void updateScoreView() {
            Platform.runLater(() -> {
                scoreWinLabel.setText("" + player.getScore());
                scoreLossLabel.setText("" + player.getLosses());
                roundNumberLabel.setText("" + player.getRoundCount());

                if (!player.canStartNewRound) return;

                rockBtn.setDisable(false);
                paperBtn.setDisable(false);
                scissorsBtn.setDisable(false);
                addLog(" --- new round!");
                player.canStartNewRound = false;
            });
        }

        @Override
        public void addLog(String message) {
            Platform.runLater(() -> {
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                logTextarea.appendText(timestamp + " - " + message + "\n");
            });
        }

        @Override
        public Player opponentWantsToJoin(Opponent opponent, ArrayList<Opponent> opponents) {
            ArrayList<Opponent> newOpponents = new ArrayList<Opponent>();
            if (player.addOpponent(opponent)) {
                newOpponents.add(opponent);
            }

            for (Opponent o : opponents) {
                if (player.addOpponent(o)) {
                    newOpponents.add(o);
                }
            }

            // update view
            Platform.runLater(() -> {
                playerCountLabel.setText("" + player.getOpponents().size());

                for (Opponent o : newOpponents) {
                    addLog(o.username + " joined");
                }
            });

            return player;
        }

        @Override
        public void opponentAnnouncesHimself(Opponent newOpponent) {
            if (!player.addOpponent(newOpponent)) return;

            Platform.runLater(() -> {
                playerCountLabel.setText("" + player.getOpponents().size());
                addLog(newOpponent.username + " joined");
            });
        }

        @Override
        public void opponentSentMove(Opponent opponent, String move, int round) {
            if (!player.addOpponentMove(opponent, move, round)) return;

            Platform.runLater(() -> {
                addLog(opponent.username + " sent a move.");
                updateScoreView();
            });
        }
    }
}
