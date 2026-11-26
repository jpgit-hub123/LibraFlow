package edu.icet.service.impl;

import edu.icet.mapper.MemberMapper;
import edu.icet.model.dto.MemberDTO;
import edu.icet.repository.AddMemberRepository;
import edu.icet.repository.impl.AddMemberRepositoryImpl;
import edu.icet.service.AddMemberService;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddMemberServiceImpl implements AddMemberService {

    private MemberDTO memberDTOToEdit;
    private final AddMemberRepository addMemberRepository = new AddMemberRepositoryImpl();

    @Override
    public void closeAddMemberForm(TextField nameField) {
        Stage addMemberWindow = (Stage) nameField.getScene().getWindow();
        addMemberWindow.close();
    }

    @Override
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

        MemberMapper memberMapper = new MemberMapper();
        MemberDTO memberDTO = new MemberDTO(name, email, phone, address);

        if (memberDTOToEdit == null) {
            if (addMemberRepository.saveMember(memberMapper.toEntity(memberDTO))) {
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
            if (!addMemberRepository.updateMember(String.valueOf(memberDTOToEdit.getMemberId()), memberMapper.toEntity(memberDTO))) {
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

    @Override
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void setEditedMemberInfoToEditMemberForm(MemberDTO selectedMemberDTO, TextField nameField,
                                                    TextField emailField, TextField phoneField,
                                                    TextArea addressField, Label titleLabel,
                                                    Label subTitleLabel, Button btnSave) {

        this.memberDTOToEdit = selectedMemberDTO;

        nameField.setText(selectedMemberDTO.getName());
        emailField.setText(selectedMemberDTO.getEmail());
        phoneField.setText(selectedMemberDTO.getPhone());
        addressField.setText(selectedMemberDTO.getAddress());

        titleLabel.setText("Update Member Form");
        subTitleLabel.setText("Enter the member details below to edit member.");
        btnSave.setText("Update Member");
    }
}
