package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
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
    public ComboBox<MemberEntity> memberComboBox;

    @FXML
    public ComboBox<BookEntity> bookComboBox;

    @FXML
    public DatePicker issueDatePicker;

    @FXML
    public DatePicker dueDatePicker;

    @FXML
    public TableColumn<BookIssueEntity, Double> fineAmountColumn;

    @FXML
    private TableColumn<BookIssueEntity, Long> overdueDaysColumn;

    @FXML
    private TableView<BookIssueEntity> issuedBooksTable;

    @FXML
    private TableColumn<BookIssueEntity, Integer> memberIdColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> isbnColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> bookTitleColumn;

    @FXML
    private TableColumn<BookIssueEntity, LocalDate> issueDateColumn;

    @FXML
    private TableColumn<BookIssueEntity, LocalDate> dueDateColumn;

    @FXML
    private TableColumn<BookIssueEntity, String> statusColumn;

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

        memberComboBox.setItems(FXCollections.observableArrayList(new MemberDTO().getAllMembers()));
        memberComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MemberEntity memberEntity) {
                if (memberEntity == null) {
                    return "";
                }
                return memberEntity.getMemberId() + " - " + memberEntity.getName();
            }

            @Override
            public MemberEntity fromString(String s) {
                return null;
            }
        });

        bookComboBox.setItems(FXCollections.observableArrayList(new BookDTO().getAllBooks()));
        bookComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(BookEntity bookEntity) {
                if (bookEntity == null) {
                    return "";
                }
                return bookEntity.getIsbn() + " - " + bookEntity.getTitle() + " (Available: " + bookEntity.getQuantity() + ")";
            }

            @Override
            public BookEntity fromString(String s) {
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
            BookIssueEntity issue = cellData.getValue();
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
