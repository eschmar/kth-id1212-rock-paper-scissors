package io.eschmann;

import io.eschmann.controller.LoginController;
import io.eschmann.model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class RockPaperScissors extends Application {
    private Player player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        // Identify local ip address
        InetAddress localAddress = InetAddress.getLocalHost();

        // Create player model
        player = new Player(localAddress.getHostAddress());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Rock Paper Scissors");
        Scene scene = new Scene(root, 240, 300);

        // get controller
        LoginController loginController = (LoginController) loader.getController();
        loginController.pewInit(player, primaryStage);

        // show scene
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        player.server.terminate();
    }
}
