package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddMemberPageController {

    @FXML
    public TextField nameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextArea addressField;

    @FXML
    public void closeFormOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void saveMemberOnAction(ActionEvent actionEvent) {
    }
}
