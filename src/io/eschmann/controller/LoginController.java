package io.eschmann.controller;

import io.eschmann.common.MessageWrapper;
import io.eschmann.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginController {
    protected Player player;
    protected Stage primaryStage;

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

        String[] input = joinInput.getText().split(":");
        Socket pewSocket = new Socket(input[0], Integer.parseInt(input[1]));

        ObjectOutputStream out = new ObjectOutputStream(pewSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(pewSocket.getInputStream());

        out.writeObject(new MessageWrapper("Pew pew pew."));
        pewSocket.close();


        System.out.println("Clicked on join.");
        // todo: send something to join socket.
    }

    protected Stage getStage() {
        return (Stage) myIpText.getScene().getWindow();
    }

    protected Player getPlayer() {
        return (Player) getStage().getUserData();
    }

    public void pewInit(Player player, Stage stage) {
        this.player = player;
        this.primaryStage = stage;

        myIpText.setText(player.ip + ":" + player.server.port);
        usernameInput.setText(player.username);
    }
}
