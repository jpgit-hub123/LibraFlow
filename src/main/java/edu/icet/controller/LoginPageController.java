package edu.icet.controller;

import javafx.fxml.FXML;
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
    private final LoginController loginController = new LoginController();

    @FXML
    private void handleLoginOnAction() {
        if (loginController.authenticate(usernameField, passwordField)) {
            loginController.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
        } else {
            System.out.println("hii");
        }
    }

    @FXML
    public void joinOnAction() {

    }
}
