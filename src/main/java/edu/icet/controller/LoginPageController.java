package edu.icet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private final LoginController loginController = new LoginController();

    @FXML
    private void handleLoginOnAction() {
        loginController.handleLoginPage(usernameField, passwordField, messageLabel);
    }
}
