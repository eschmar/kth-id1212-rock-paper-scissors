package io.eschmann;

import io.eschmann.model.Player;
import io.eschmann.net.server.ReceiverServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class RockPaperScissors extends Application {
    private Player player;
    private ReceiverServer server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        // Identify local ip address
        InetAddress localAddress = InetAddress.getLocalHost();

        // Start new receiver process
        server = new ReceiverServer();
        server.start();

        // Create player model
        player = new Player(localAddress.getHostAddress(), server.port);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        primaryStage.setTitle("Rock Paper Scissors");

        Scene scene = new Scene(root, 240, 275);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
