package edu.icet.controller;

import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.service.DashboardService;
import edu.icet.service.impl.DashboardServiceImpl;
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
    private TableView<BookIssueDTO> recentTransactionsTable;

    @FXML
    private TableColumn<BookIssueDTO, Integer> transactionMemberColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> transactionBookColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> transactionTypeColumn;

    @FXML
    private TableColumn<BookIssueDTO, LocalDate> transactionDateColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> transactionStatusColumn;

    @FXML
    public final DashboardService dashboardService = new DashboardServiceImpl();

    @FXML
    private void showDashboardOnAction() {
        dashboardService.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
    }

    @FXML
    private void showBooksOnAction() {
        dashboardService.loadPage("/edu/icet/view/Books.fxml", rootPane);
    }

    @FXML
    public void showMembersOnAction() {
        dashboardService.loadPage("/edu/icet/view/Members.fxml", rootPane);
    }

    @FXML
    public void showIssueReturnOnAction() {
        dashboardService.loadPage("/edu/icet/view/IssueReturn.fxml", rootPane);
    }

    @FXML
    public void logoutOnAction() {
        dashboardService.loadPage("/edu/icet/view/LoginPage.fxml", rootPane);
    }

    @FXML
    public void initialize() {
        int totalBooks = dashboardService.getTotalBooksCount();
        totalBooksLabel.setText(String.valueOf(totalBooks));

        int totalQuantity = dashboardService.getTotalQuantityCount();
        totalQuantityLabel.setText(String.valueOf(totalQuantity));

        int totalCategories = dashboardService.getTotalCategories();
        totalCategoryLabel.setText(String.valueOf(totalCategories));

        int totalMembers = dashboardService.getTotalMemberCount();
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
            BookIssueDTO issue = cellData.getValue();

            String type = "RETURNED".equals(issue.getStatus())
                    ? "Return"
                    : "Issue";

            return new SimpleStringProperty(type);
        });

        transactionDateColumn.setCellValueFactory(cellData -> {
            BookIssueDTO issue = cellData.getValue();

            LocalDate date = "RETURNED".equals(issue.getStatus())
                    ? issue.getReturnDate()
                    : issue.getIssueDate();

            return new SimpleObjectProperty<>(date);
        });

        transactionStatusColumn.setCellValueFactory(cellData -> {
            BookIssueDTO issue = cellData.getValue();

            String status = issue.isOverdue()
                    ? "OVERDUE"
                    : issue.getStatus();

            return new SimpleStringProperty(status);
        });

        dashboardService.loadRecentTransactions(recentTransactionsTable);
    }
}
