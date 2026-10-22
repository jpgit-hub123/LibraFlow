package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class DashboardPageController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label totalQuantityLabel;

    @FXML
    private Label totalCategoryLabel;

    @FXML
    private Label totalMembersLabel;

    public DashboardController dashboardController = new DashboardController();

    @FXML
    private void showDashboardOnAction() {
        dashboardController.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
    }

    @FXML
    private void showBooksOnAction() {
        dashboardController.loadPage("/edu/icet/view/Books.fxml", rootPane);
    }

    @FXML
    public void showMembersOnAction(ActionEvent actionEvent) {
        dashboardController.loadPage("/edu/icet/view/Members.fxml", rootPane);
    }

    @FXML
    public void showIssueReturnOnAction(ActionEvent actionEvent) {
        dashboardController.loadPage("/edu/icet/view/IssueReturn.fxml", rootPane);
    }

    @FXML
    public void initialize() {
        int totalBooks = dashboardController.getTotalBooksCount();
        totalBooksLabel.setText(String.valueOf(totalBooks));

        int totalQuantity = dashboardController.getTotalQuantityCount();
        totalQuantityLabel.setText(String.valueOf(totalQuantity));

        int totalCategories = dashboardController.getTotalCategories();
        totalCategoryLabel.setText(String.valueOf(totalCategories));

        int totalMembers = dashboardController.getTotalMemberCount();
        totalMembersLabel.setText(String.valueOf(totalMembers));
    }
}