package edu.icet.service;

import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.MemberEntity;
import javafx.scene.control.*;

public interface AddMemberService {

    void closeAddMemberForm(TextField nameField);

    void registerNewMember(TextField nameField, TextField emailField, TextField phoneField, TextArea addressField);

    void showAlert(Alert.AlertType alertType, String title, String message);

    void setEditedMemberInfoToEditMemberForm(MemberDTO selectedMemberDTO, TextField nameField, TextField emailField,
                                             TextField phoneField, TextArea addressField, Label titleLabel,
                                             Label subTitleLabel, Button btnSave);
}
