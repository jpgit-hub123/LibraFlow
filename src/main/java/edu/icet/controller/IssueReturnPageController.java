package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.dto.MemberDTO;
import edu.icet.service.IssueReturnService;
import edu.icet.service.impl.IssueReturnServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class IssueReturnPageController {

    @FXML
    public ComboBox<MemberDTO> memberComboBox;

    @FXML
    public ComboBox<BookDTO> bookComboBox;

    @FXML
    public DatePicker issueDatePicker;

    @FXML
    public DatePicker dueDatePicker;

    @FXML
    public TableColumn<BookIssueDTO, Double> fineAmountColumn;

    @FXML
    private TableColumn<BookIssueDTO, Long> overdueDaysColumn;

    @FXML
    private TableView<BookIssueDTO> issuedBooksTable;

    @FXML
    private TableColumn<BookIssueDTO, Integer> memberIdColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> isbnColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> bookTitleColumn;

    @FXML
    private TableColumn<BookIssueDTO, LocalDate> issueDateColumn;

    @FXML
    private TableColumn<BookIssueDTO, LocalDate> dueDateColumn;

    @FXML
    private TableColumn<BookIssueDTO, String> statusColumn;

    @FXML
    private Label totalIssuedLabel;

    @FXML
    private Label borrowedLabel;

    @FXML
    private Label overdueLabel;

    @FXML
    private Label returnedLabel;

    @FXML
    public IssueReturnService issueReturnService = new IssueReturnServiceImpl();

    @FXML
    public void initialize() {
        memberComboBox.setItems(FXCollections.observableArrayList(issueReturnService.getAllMembers()));
        memberComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MemberDTO memberDTO) {
                if (memberDTO == null) {
                    return "";
                }
                return memberDTO.getMemberId() + " - " + memberDTO.getName();
            }

            @Override
            public MemberDTO fromString(String s) {
                return null;
            }
        });
        bookComboBox.setItems(FXCollections.observableArrayList(issueReturnService.getAllBooks()));
        bookComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(BookDTO bookDTO) {
                if (bookDTO == null) {
                    return "";
                }
                return bookDTO.getIsbn() + " - " + bookDTO.getTitle() + " (Available: " + bookDTO.getQuantity() + ")";
            }

            @Override
            public BookDTO fromString(String s) {
                return null;
            }
        });

        issueDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(14));

        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(cellData -> {
            BookIssueDTO issue = cellData.getValue();
            String displayStatus;
            if (issue.isOverdue()) {
                displayStatus = "OVERDUE";
            } else {
                displayStatus = issue.getStatus();
            }
            return new javafx.beans.property.SimpleStringProperty(displayStatus);
        });
        overdueDaysColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(
                        cellData.getValue().getOverdueDays()
                ).asObject()
        );
        fineAmountColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getFineAmount()
                ).asObject()
        );
        issueReturnService.loadIssuedBooks(issuedBooksTable);
        issueReturnService.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void showDashboardOnAction() {
        issueReturnService.loadPage("/edu/icet/view/Dashboard.fxml", memberComboBox);
    }

    @FXML
    public void showBooksOnAction() {
        issueReturnService.loadPage("/edu/icet/view/Books.fxml", memberComboBox);
    }

    @FXML
    public void showMembersOnAction() {
        issueReturnService.loadPage("/edu/icet/view/Members.fxml", memberComboBox);
    }

    @FXML
    public void logoutOnAction() {
        issueReturnService.loadPage("/edu/icet/view/LoginPage.fxml", memberComboBox);
    }

    @FXML
    public void issueBookOnAction() {
        issueReturnService.issuedTheBook(memberComboBox, bookComboBox, issueDatePicker, dueDatePicker, issuedBooksTable);
        issueReturnService.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void returnBookOnAction() {
        issueReturnService.returnBook(issuedBooksTable, bookComboBox, memberComboBox);
        issueReturnService.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void showOverdueBooksOnAction() {
        issueReturnService.showOverdueBooks(issuedBooksTable);
    }

    @FXML
    public void showAllIssuesOnAction() {
        issueReturnService.loadIssuedBooks(issuedBooksTable);
    }
}
