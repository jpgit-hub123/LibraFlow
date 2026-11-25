package edu.icet.controller;

import edu.icet.model.dto.MemberDTO;
import edu.icet.service.MembersService;
import edu.icet.service.impl.MembersServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MembersPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<MemberDTO> membersTable;

    @FXML
    private TableColumn<MemberDTO, Number> memberIdColumn;

    @FXML
    private TableColumn<MemberDTO, String> nameColumn;

    @FXML
    private TableColumn<MemberDTO, String> emailColumn;

    @FXML
    private TableColumn<MemberDTO, String> phoneColumn;

    @FXML
    private TableColumn<MemberDTO, String> addressColumn;

    @FXML
    public MembersService membersServiceImpl = new MembersServiceImpl();

    @FXML
    public void initialize() {
        membersServiceImpl.setMembersDataIntoTable(searchField, membersTable, memberIdColumn, nameColumn, emailColumn, phoneColumn, addressColumn);
    }

    @FXML
    private void searchMembers() {
        membersServiceImpl.getSearchMember(searchField, membersTable);
    }

    @FXML
    private void addMemberOnAction() {
        membersServiceImpl.loadForm(searchField, membersTable);
    }

    @FXML
    private void editMemberOnAction() {
        membersServiceImpl.editMembersInfo(searchField, membersTable);
    }

    @FXML
    private void deleteMemberOnAction() {
        membersServiceImpl.removeMember(searchField, membersTable);
    }

    @FXML
    public void showDashboardOnAction() {
        membersServiceImpl.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    @FXML
    public void showBooksOnAction() {
        membersServiceImpl.loadPage("/edu/icet/view/Books.fxml", searchField);
    }

    @FXML
    public void showIssueReturnOnAction() {
        membersServiceImpl.loadPage("/edu/icet/view/IssueReturn.fxml", searchField);
    }

    @FXML
    public void logoutOnAction() {
        membersServiceImpl.loadPage("/edu/icet/view/LoginPage.fxml", searchField);
    }
}