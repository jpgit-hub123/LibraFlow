package edu.icet.service;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public interface LoginService {

    void loadPage(String fxmlFile, BorderPane rootPane);

    boolean authenticate(TextField usernameField, TextField passwordField);
}
