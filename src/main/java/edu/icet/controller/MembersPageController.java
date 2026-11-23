package edu.icet.controller;

import edu.icet.model.entity.MemberEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MembersPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<MemberEntity> membersTable;

    @FXML
    private TableColumn<MemberEntity, Number> memberIdColumn;

    @FXML
    private TableColumn<MemberEntity, String> nameColumn;

    @FXML
    private TableColumn<MemberEntity, String> emailColumn;

    @FXML
    private TableColumn<MemberEntity, String> phoneColumn;

    @FXML
    private TableColumn<MemberEntity, String> addressColumn;

    public MembersController membersController = new MembersController();

    @FXML
    public void initialize() {
        membersController.setMembersDataIntoTable(searchField, membersTable, memberIdColumn, nameColumn, emailColumn, phoneColumn, addressColumn);
    }

    @FXML
    private void searchMembers() {
        membersController.getSearchMember(searchField, membersTable);
    }

    @FXML
    private void addMemberOnAction() {
        membersController.loadForm(searchField, membersTable);
    }

    @FXML
    private void editMemberOnAction() {
        membersController.editMembersInfo(searchField, membersTable);
    }

    @FXML
    private void deleteMemberOnAction() {
        membersController.removeMember(searchField, membersTable);
    }

    @FXML
    public void showDashboardOnAction() {
        membersController.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    @FXML
    public void showBooksOnAction() {
        membersController.loadPage("/edu/icet/view/Books.fxml", searchField);
    }

    @FXML
    public void showIssueReturnOnAction() {
        membersController.loadPage("/edu/icet/view/IssueReturn.fxml", searchField);
    }

    @FXML
    public void logoutOnAction() {
        membersController.loadPage("/edu/icet/view/LoginPage.fxml", searchField);
    }

}