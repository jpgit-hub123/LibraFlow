package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LoginPageController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private final LoginController loginController = new LoginController();

    @FXML
    private void handleLoginOnAction() {
        if (loginController.authenticate(usernameField, passwordField)) {
            loginController.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
        } else {
            System.out.println("hii");
        }
    }

    public void joinOnAction(ActionEvent actionEvent) {
//        loginController.loadPage("/edu/icet/view/RegisterUser.fxml", rootPane);
    }
}
