package edu.icet.controller;

import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.MemberEntity;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddMemberController {

    private final MemberDTO memberDTO = new MemberDTO();
    private MemberEntity memberEntityToEdit;

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

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please fill in all fields"
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter a valid email address."
            );
            emailField.requestFocus();
            return;
        }

        if (memberEntityToEdit == null) {
            MemberEntity memberEntity = new MemberEntity(name, email, phone, address);

            if (memberDTO.saveMember(memberEntity)) {
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Member registered successfully!"
                );
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Save Failed",
                        "Unable to register the member. Please check whether the email already exists."
                );
                return;
            }
        } else {
            if (!memberDTO.updateMember(String.valueOf(memberEntityToEdit.getMemberId()), new MemberEntity(name, email, phone, address))) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to update the member. Please try again."
                );
            }
        }

        Stage currentWindow = (Stage) nameField.getScene().getWindow();
        currentWindow.close();

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setEditedMemberInfoToEditMemberForm(MemberEntity selectedMemberEntity, TextField nameField,
                                                    TextField emailField, TextField phoneField,
                                                    TextArea addressField, Label titleLabel,
                                                    Label subTitleLabel, Button btnSave) {

        this.memberEntityToEdit = selectedMemberEntity;

        nameField.setText(selectedMemberEntity.getName());
        emailField.setText(selectedMemberEntity.getEmail());
        phoneField.setText(selectedMemberEntity.getPhone());
        addressField.setText(selectedMemberEntity.getAddress());

        titleLabel.setText("Update Member Form");
        subTitleLabel.setText("Enter the member details below to edit member.");
        btnSave.setText("Update Member");
    }
}
