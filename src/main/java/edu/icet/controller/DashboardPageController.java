package edu.icet.controller;

import edu.icet.model.entity.BookIssueEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;

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

    @FXML
    private TableView<BookIssueEntity> recentTransactionsTable;

    @FXML
    private TableColumn<BookIssueEntity, Integer> transactionMemberColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> transactionBookColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> transactionTypeColumn;

    @FXML
    private TableColumn<BookIssueEntity, LocalDate> transactionDateColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> transactionStatusColumn;

    @FXML
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
    public void showMembersOnAction() {
        dashboardController.loadPage("/edu/icet/view/Members.fxml", rootPane);
    }

    @FXML
    public void showIssueReturnOnAction() {
        dashboardController.loadPage("/edu/icet/view/IssueReturn.fxml", rootPane);
    }

    public void logoutOnAction() {
        dashboardController.loadPage("/edu/icet/view/LoginPage.fxml", rootPane);
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

        recentTransactionsTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        transactionMemberColumn.setCellValueFactory(
                new PropertyValueFactory<>("memberId")
        );

        transactionBookColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle")
        );

        transactionTypeColumn.setCellValueFactory(cellData -> {
            BookIssueEntity issue = cellData.getValue();

            String type = "RETURNED".equals(issue.getStatus())
                    ? "Return"
                    : "Issue";

            return new SimpleStringProperty(type);
        });

        transactionDateColumn.setCellValueFactory(cellData -> {
            BookIssueEntity issue = cellData.getValue();

            LocalDate date = "RETURNED".equals(issue.getStatus())
                    ? issue.getReturnDate()
                    : issue.getIssueDate();

            return new SimpleObjectProperty<>(date);
        });

        transactionStatusColumn.setCellValueFactory(cellData -> {
            BookIssueEntity issue = cellData.getValue();

            String status = issue.isOverdue()
                    ? "OVERDUE"
                    : issue.getStatus();

            return new SimpleStringProperty(status);
        });

        dashboardController.loadRecentTransactions(recentTransactionsTable);
    }
}
