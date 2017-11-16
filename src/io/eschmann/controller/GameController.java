package io.eschmann.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GameController {
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

}
