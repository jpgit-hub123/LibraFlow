package edu.icet.service.impl;

import edu.icet.controller.AddMemberPageController;
import edu.icet.mapper.MemberMapper;
import edu.icet.model.dto.MemberDTO;
import edu.icet.repository.MembersRepository;
import edu.icet.repository.impl.MembersRepositoryImpl;
import edu.icet.service.MembersService;
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

public class MembersServiceImpl implements MembersService {

    private final MembersRepository membersRepository = new MembersRepositoryImpl();

    @Override
    public void setMembersDataIntoTable(TextField searchField, TableView<MemberDTO> membersTable,
                                        TableColumn<MemberDTO, Number> memberIdColumn,
                                        TableColumn<MemberDTO, String> nameColumn,
                                        TableColumn<MemberDTO, String> emailColumn,
                                        TableColumn<MemberDTO, String> phoneColumn,
                                        TableColumn<MemberDTO, String> addressColumn) {

        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadMembers(searchField, membersTable);
    }

    @Override
    public void loadMembers(TextField searchField, TableView<MemberDTO> membersTable) {
        getSearchMember(searchField, membersTable);
    }

    @Override
    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent rootNode = loader.load();
            searchField.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadForm(TextField searchField, TableView<MemberDTO> membersTable) {

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

    @Override
    public void editMembersInfo(TextField searchField, TableView<MemberDTO> membersTable) {
        MemberDTO selectedMemberDTO = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMemberDTO == null) {
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
            addMemberPageController.selectedMemberToEdit(selectedMemberDTO);
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

    @Override
    public void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void removeMember(TextField searchField, TableView<MemberDTO> membersTable) {
        MemberDTO selectedMemberDTO = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMemberDTO == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Member Selected",
                    "Please select a member to delete."
            );
            return;
        }

        if (membersRepository.hasIssueHistory(selectedMemberDTO.getMemberId())) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Cannot Delete Member",
                    "This Member has transaction history. Therefore, cannot be deleted!"
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Member");
        confirmation.setHeaderText("Delete " + selectedMemberDTO.getName() + "?");
        confirmation.setContentText("Are you sure you want to delete this Member?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (membersRepository.deleteMember(String.valueOf(selectedMemberDTO.getMemberId()))) {
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

    @Override
    public void getSearchMember(TextField searchField, TableView<MemberDTO> membersTable) {
        final String searchData = searchField.getText().trim();
        ObservableList<MemberDTO> filteredMemberDTOList = FXCollections.observableArrayList();
        MemberMapper memberMapper = new MemberMapper();
        ObservableList<MemberDTO> membersDTOList = FXCollections.observableArrayList(
                membersRepository.getAllMembers().stream().map(memberMapper::toDTO).toList());
        for (MemberDTO memberDTO : membersDTOList) {
            if (memberDTO.getName().toLowerCase().contains(searchData)
                || memberDTO.getEmail().toLowerCase().contains(searchData)
                || memberDTO.getPhone().toLowerCase().contains(searchData)) {
                filteredMemberDTOList.add(memberDTO);
            }
        }
        membersTable.setItems(filteredMemberDTOList);
    }
}