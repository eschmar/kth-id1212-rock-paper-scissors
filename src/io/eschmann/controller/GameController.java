package io.eschmann.controller;

import io.eschmann.model.Player;
import io.eschmann.net.server.Observer;
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

public class GameController {
    protected Player player;
    protected Stage primaryStage;
    protected ReceiverServer server;

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
    public void init(Player player, Stage stage, ReceiverServer server) {
        this.player = player;
        this.primaryStage = stage;
        this.server = server;

        server.username = player.username;
        server.observer = new GameObserver();
        server.start();

//        System.out.println("Thread after server start: " + Thread.currentThread().getId());

        myIpText.setText(player.ip + ":" + player.port);
        usernameLabel.setText(player.username);
    }


    private class GameObserver implements Observer {
        @Override
        public void updateThings() {
            Platform.runLater(() -> {
                System.out.println("Updated things!");
                scoreWinLabel.setText("35");
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
