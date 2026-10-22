package edu.icet.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMemberController {

    public void closeAddMemberForm(TextField nameField) {
        Stage addMemberWindow = (Stage) nameField.getScene().getWindow();
        addMemberWindow.close();
    }

    public void registerNewMember(TextField nameField, TextField emailField, TextField phoneField,
                                  TextArea addressField) {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();


    }
}
