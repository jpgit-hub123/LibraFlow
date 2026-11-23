package edu.icet.repository;

import javafx.scene.control.TextField;

public interface LoginRepository {

    boolean checkLoginDetails(TextField usernameField, TextField passwordField);
}
