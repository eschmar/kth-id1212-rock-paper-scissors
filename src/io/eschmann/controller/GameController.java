package io.eschmann.controller;

import io.eschmann.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {
    protected Player player;
    protected Stage primaryStage;

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
    public void init(Player player, Stage stage) {
        this.player = player;
        this.primaryStage = stage;

        myIpText.setText(player.ip + ":" + player.server.port);
        usernameLabel.setText(player.username);
    }
}
