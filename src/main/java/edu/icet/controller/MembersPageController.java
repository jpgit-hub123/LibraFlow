package edu.icet.controller;

import edu.icet.model.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MembersPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Member> membersTable;

    @FXML
    private TableColumn<Member, Number> memberIdColumn;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TableColumn<Member, String> phoneColumn;

    @FXML
    private TableColumn<Member, String> addressColumn;

    public MembersController membersController = new MembersController();



    @FXML
    public void initialize() {
        membersController.setMembersDataIntoTable(membersTable, memberIdColumn, nameColumn, emailColumn, phoneColumn, addressColumn);
    }

    @FXML
    private void searchMembers() {
    }

    @FXML
    private void addMemberOnAction() {

    }

    @FXML
    private void editMemberOnAction() {
    }

    @FXML
    private void deleteMemberOnAction() {
    }

    @FXML
    public void showDashboardOnAction(ActionEvent actionEvent) {
        membersController.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    public void showBooksOnAction(ActionEvent actionEvent) {
        membersController.loadPage("/edu/icet/view/Books.fxml", searchField);
    }

    public void showIssueReturnOnAction(ActionEvent actionEvent) {
        membersController.loadPage("/edu/icet/view/IssueReturn.fxml", searchField);
    }
}