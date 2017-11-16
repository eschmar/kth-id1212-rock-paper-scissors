package io.eschmann.controller;

import io.eschmann.common.Message;
import io.eschmann.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    protected void changeToGameState(boolean clickedJoin) throws IOException {
        // update username
        player.username = usernameInput.getText();

        // load new view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/game.fxml"));
        Parent root = loader.load();

        // get controller
        GameController gameController = (GameController) loader.getController();
        gameController.init(player, primaryStage);

        // show scene
        primaryStage.getScene().setRoot(root);
        primaryStage.show();

        if (!clickedJoin) return;

        String[] input = joinInput.getText().split(":");
        Socket pewSocket = new Socket(input[0], Integer.parseInt(input[1]));

        ObjectOutputStream out = new ObjectOutputStream(pewSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(pewSocket.getInputStream());

        out.writeObject(new Message("join", "Pew pew pew."));
        pewSocket.close();


        System.out.println("Clicked on join.");
        // todo: send something to join socket.
    }

    /**
     * Updates the view with player information and makes the player and stage available to the controller.
     * @param player
     * @param stage
     */
    public void init(Player player, Stage stage) {
        this.player = player;
        this.primaryStage = stage;

        myIpText.setText(player.ip + ":" + player.server.port);
        usernameInput.setText(player.username);
    }
}
