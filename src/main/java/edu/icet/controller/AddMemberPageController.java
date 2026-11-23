package edu.icet.controller;

import edu.icet.model.entity.MemberEntity;
import edu.icet.service.AddMemberService;
import edu.icet.service.impl.AddMemberServiceImpl;
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
    public final AddMemberService addMemberService = new AddMemberServiceImpl();

    @FXML
    public Label titleLabel;

    @FXML
    public Label subTitleLabel;

    @FXML
    public Button btnSave;

    @FXML
    public void closeFormOnAction() {
        addMemberService.closeAddMemberForm(nameField);
    }

    @FXML
    public void saveMemberOnAction() {
        addMemberService.registerNewMember(nameField, emailField, phoneField, addressField);
    }

    @FXML
    public void selectedMemberToEdit(MemberEntity selectedMemberEntity) {
        addMemberService.setEditedMemberInfoToEditMemberForm(selectedMemberEntity, nameField, emailField, phoneField,
                addressField, titleLabel, subTitleLabel, btnSave);
    }
}
