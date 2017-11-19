package io.eschmann.controller;

import io.eschmann.model.Opponent;
import io.eschmann.model.Player;
import io.eschmann.net.client.OpponentConnection;
import io.eschmann.net.common.Observer;
import io.eschmann.net.server.ReceiverServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class GameController {
    protected Player player;
    protected Stage primaryStage;
    protected ReceiverServer server;
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
    public void init(Player player, Stage stage, ReceiverServer server, Opponent opponent) {
        this.player = player;
        this.primaryStage = stage;
        this.server = server;

        // start receiver server
        server.username = player.username;
        server.observer = new GameObserver();
        server.start();

        // update view with ip and username
        myIpText.setText(player.ip + ":" + player.port);
        usernameLabel.setText(player.username);

        if (opponent == null) return;

        CompletableFuture.runAsync(() -> {
            try {
                opponentConnection.connect(opponent);
                opponentConnection.sendJoinMessage(player, new GameObserver());
                opponentConnection.disconnect();
            } catch (Exception e) {
                System.out.println("Unable to join game.");
            }
        });
    }

    private class GameObserver implements Observer {
        @Override
        public void updateScoreView() {
            Platform.runLater(() -> {
                scoreWinLabel.setText("" + player.getScore());
                scoreLossLabel.setText("" + player.getLosses());
                roundNumberLabel.setText("" + player.getRoundCount());
                playerCountLabel.setText("" + player.getOpponents().size());
                System.out.println("Updated things!");
            });
        }

        @Override
        public void addLog(String message) {
            Platform.runLater(() -> {
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                logTextarea.appendText(timestamp + " - " + message + "\n");
            });
        }
    }
}
