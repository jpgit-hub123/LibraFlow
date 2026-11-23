package edu.icet.controller;

import edu.icet.service.LoginService;
import edu.icet.service.impl.LoginServiceImpl;
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
    private final LoginService loginService = new LoginServiceImpl();

    @FXML
    private void handleLoginOnAction() {
        if (loginService.authenticate(usernameField, passwordField)) {
            loginService.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
        } else {
            System.out.println("hii");
        }
    }

    @FXML
    public void joinOnAction() {

    }
}
