package edu.icet.controller;

import edu.icet.dao.MemberDAO;
import edu.icet.model.Member;
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

    public void setMembersDataIntoTable(TableView<Member> membersTable,
                                        TableColumn<Member, Number> memberIdColumn,
                                        TableColumn<Member, String> nameColumn,
                                        TableColumn<Member, String> emailColumn,
                                        TableColumn<Member, String> phoneColumn,
                                        TableColumn<Member, String> addressColumn) {

        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadMembers(membersTable);
    }

    public void loadMembers(TableView<Member> membersTable) {
        MemberDAO memberDAO = new MemberDAO();
        ObservableList<Member> membersList = FXCollections.observableArrayList(memberDAO.getAllMembers());
        membersTable.setItems(membersList);
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

    public void loadForm(TableView<Member> membersTable) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddMember.fxml"));
            Parent root = loader.load();
            Stage addMemberStage = new Stage();
            addMemberStage.setTitle("LibraFlow | Add New Member");
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.setScene(new Scene(root));
            addMemberStage.setResizable(false);
            addMemberStage.showAndWait();
            loadMembers(membersTable);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMembersInfo(TableView<Member> membersTable) {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Book Selected",
                    "Please select a book to delete."
            );
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddMember.fxml"));
        try {
            Parent root = loader.load();
            AddMemberPageController addMemberPageController = loader.getController();
            addMemberPageController.selectedMemberToEdit(selectedMember);
            Stage addMemberStage = new Stage();
            addMemberStage.setTitle("LibraFlow | Update Member");
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.setScene(new Scene(root));
            addMemberStage.setResizable(false);
            addMemberStage.showAndWait();
            loadMembers(membersTable);
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

    public void removeMember(TableView<Member> membersTable) {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Book Selected",
                    "Please select a book to delete."
            );
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Member");
        confirmation.setHeaderText("Delete " + selectedMember.getName() + "?");
        confirmation.setContentText("Are you sure you want to delete this Member?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (new MemberDAO().deleteMember(String.valueOf(selectedMember.getMemberId()))) {
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Book Deleted",
                            "The book was deleted successfully."
                    );
                    loadMembers(membersTable);
                }
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to delete the book from the database."
                );
            }
        });
    }
}