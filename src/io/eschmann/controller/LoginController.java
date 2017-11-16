package io.eschmann.controller;

import io.eschmann.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    public TextField myIpText;

    @FXML
    public TextField usernameInput;

    @FXML
    public TextField joinInput;

    public void onNewGameAction(ActionEvent actionEvent) throws IOException {
        changeToGameState();
    }

    public void onJoinGameAction(ActionEvent actionEvent) throws IOException {
        changeToGameState(true);
    }

    protected void changeToGameState() throws IOException {
        changeToGameState(false);
    }

    public void updatePlayerIp() {
        Player player = getPlayer();
        myIpText.setText(player.ip + ":" + player.server.port);
    }

    protected void changeToGameState(boolean clickedJoin) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/game.fxml"));
        Stage primaryStage = getStage();

        Player player = (Player) primaryStage.getUserData();
        player.username = usernameInput.getText();
        primaryStage.getScene().setRoot(root);

        if (!clickedJoin) return;

        System.out.println("Clicked on join.");
        // todo: send something to join socket.
    }

    protected Stage getStage() {
        return (Stage) myIpText.getScene().getWindow();
    }

    protected Player getPlayer() {
        return (Player) getStage().getUserData();
    }
}
