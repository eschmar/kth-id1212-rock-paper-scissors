package io.eschmann.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginController {
    @FXML
    public TextField myIpText;

    public void pewAction(ActionEvent event) {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            String address = localAddress.getHostAddress();
            System.out.println(address);
            myIpText.setText(address);
//            ownIpLabel.setText("Pew");
        } catch (UnknownHostException e) {

        }
    }
}
