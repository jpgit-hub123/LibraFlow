package edu.icet.controller;

import edu.icet.model.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public AddMemberController addMemberController = new AddMemberController();

    @FXML
    public Label titleLabel;

    @FXML
    public Label subTitleLabel;

    @FXML
    public Button btnSave;

    @FXML
    public void closeFormOnAction(ActionEvent actionEvent) {
        addMemberController.closeAddMemberForm(nameField);
    }

    @FXML
    public void saveMemberOnAction(ActionEvent actionEvent) {
        addMemberController.registerNewMember(nameField, emailField, phoneField, addressField);
//        addMemberController.createUsernameAndPassword();
    }

    @FXML
    public void selectedMemberToEdit(Member selectedMember) {
        addMemberController.setEditedMemberInfoToEditMemberForm(selectedMember, nameField, emailField, phoneField,
                addressField, titleLabel, subTitleLabel, btnSave);
    }

}
