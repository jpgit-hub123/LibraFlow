package edu.icet.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LoginController {

    public void loadPage(String fxmlFile, BorderPane rootPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Parent rootNode = loader.load();
            rootPane.getScene().setRoot(rootNode);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(TextField usernameField, TextField passwordField) {
        return (usernameField.getText().equals("admin") && passwordField.getText().equals("admin123"));
    }
}
