package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class IssueReturnPageController {

    @FXML
    private TextField memberIdField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField issueDateField;

    @FXML
    private TextField dueDateField;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> issuedBooksTable;

    @FXML
    private TableColumn<?, ?> memberIdColumn;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> bookTitleColumn;

    @FXML
    private TableColumn<?, ?> issueDateColumn;

    @FXML
    private TableColumn<?, ?> dueDateColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    public IssueReturnController issueReturnController = new IssueReturnController();

    @FXML
    public void initialize() {
        System.out.println("Issue & Return page loaded successfully!");
    }

    public void showDashboardOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    public void showBooksOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Books.fxml", searchField);
    }

    public void showMembersOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Members.fxml", searchField);
    }
}
