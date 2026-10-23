package edu.icet.controller;

import edu.icet.dao.MemberDAO;
import edu.icet.model.Member;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddMemberController {

    private final MemberDAO memberDAO = new MemberDAO();
    private Member memberToEdit;

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

        if (memberToEdit == null) {
            Member member = new Member(name, email, phone, address);

            if (memberDAO.saveMember(member)) {
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
            if (!memberDAO.updateMember(String.valueOf(memberToEdit.getMemberId()), new Member(name, email, phone, address))) {
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

    public void setEditedMemberInfoToEditMemberForm(Member selectedMember, TextField nameField,
                                                    TextField emailField, TextField phoneField,
                                                    TextArea addressField, Label titleLabel,
                                                    Label subTitleLabel, Button btnSave) {

        this.memberToEdit = selectedMember;

        nameField.setText(selectedMember.getName());
        emailField.setText(selectedMember.getEmail());
        phoneField.setText(selectedMember.getPhone());
        addressField.setText(selectedMember.getAddress());

        titleLabel.setText("Update Member Form");
        subTitleLabel.setText("Enter the member details below to edit member.");
        btnSave.setText("Update Member");
    }
}
