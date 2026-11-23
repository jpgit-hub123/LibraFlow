package edu.icet.controller;

import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.MemberEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MembersController {

    MemberDTO memberDTO = new MemberDTO();

    public void setMembersDataIntoTable(TextField searchField, TableView<MemberEntity> membersTable,
                                        TableColumn<MemberEntity, Number> memberIdColumn,
                                        TableColumn<MemberEntity, String> nameColumn,
                                        TableColumn<MemberEntity, String> emailColumn,
                                        TableColumn<MemberEntity, String> phoneColumn,
                                        TableColumn<MemberEntity, String> addressColumn) {

        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadMembers(searchField, membersTable);
    }

    public void loadMembers(TextField searchField, TableView<MemberEntity> membersTable) {
        getSearchMember(searchField, membersTable);
    }

    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent rootNode = loader.load();
            searchField.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadForm(TextField searchField, TableView<MemberEntity> membersTable) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddMember.fxml"));
            Parent root = loader.load();
            Stage addMemberStage = new Stage();
            addMemberStage.setTitle("LibraFlow | Add New Member");
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.setScene(new Scene(root));
            addMemberStage.setResizable(false);
            addMemberStage.showAndWait();
            loadMembers(searchField, membersTable);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMembersInfo(TextField searchField, TableView<MemberEntity> membersTable) {
        MemberEntity selectedMemberEntity = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMemberEntity == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Member Selected",
                    "Please select a Member to edit."
            );
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddMember.fxml"));
        try {
            Parent root = loader.load();
            AddMemberPageController addMemberPageController = loader.getController();
            addMemberPageController.selectedMemberToEdit(selectedMemberEntity);
            Stage addMemberStage = new Stage();
            addMemberStage.setTitle("LibraFlow | Update Member");
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.setScene(new Scene(root));
            addMemberStage.setResizable(false);
            addMemberStage.showAndWait();
            loadMembers(searchField, membersTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeMember(TextField searchField, TableView<MemberEntity> membersTable) {
        MemberEntity selectedMemberEntity = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMemberEntity == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Member Selected",
                    "Please select a member to delete."
            );
            return;
        }

        if (memberDTO.hasIssueHistory(selectedMemberEntity.getMemberId())) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Cannot Delete Member",
                    "This Member has transaction history. Therefore, cannot be deleted!"
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Member");
        confirmation.setHeaderText("Delete " + selectedMemberEntity.getName() + "?");
        confirmation.setContentText("Are you sure you want to delete this Member?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (memberDTO.deleteMember(String.valueOf(selectedMemberEntity.getMemberId()))) {
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Member Deleted",
                            "The Member was deleted successfully."
                    );
                    loadMembers(searchField, membersTable);
                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Database Error",
                            "Failed to delete the Member from the database."
                    );
                }
            }
        });
    }

    public void getSearchMember(TextField searchField, TableView<MemberEntity> membersTable) {
        final String searchData = searchField.getText().trim();
        ObservableList<MemberEntity> filteredMemberEntityList = FXCollections.observableArrayList();
        ObservableList<MemberEntity> membersList = FXCollections.observableArrayList(memberDTO.getAllMembers());
        for (MemberEntity memberEntity : membersList) {
            if (memberEntity.getName().toLowerCase().contains(searchData)
                || memberEntity.getEmail().toLowerCase().contains(searchData)
                || memberEntity.getPhone().toLowerCase().contains(searchData)) {

                filteredMemberEntityList.add(memberEntity);
            }
        }
        membersTable.setItems(filteredMemberEntityList);
    }
}