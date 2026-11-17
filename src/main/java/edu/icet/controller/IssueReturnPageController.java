package edu.icet.controller;

import edu.icet.dao.BookDAO;
import edu.icet.dao.MemberDAO;
import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import edu.icet.model.Member;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class IssueReturnPageController {

    @FXML
    public ComboBox<Member> memberComboBox;

    @FXML
    public ComboBox<Book> bookComboBox;

    @FXML
    public DatePicker issueDatePicker;

    @FXML
    public DatePicker dueDatePicker;

    @FXML
    public TableColumn<BookIssue, Double> fineAmountColumn;

    @FXML
    private TableColumn<BookIssue, Long> overdueDaysColumn;

    @FXML
    private TableView<BookIssue> issuedBooksTable;

    @FXML
    private TableColumn<BookIssue, Integer> memberIdColumn;

    @FXML
    private TableColumn<BookIssue, String> isbnColumn;

    @FXML
    private TableColumn<BookIssue, String> bookTitleColumn;

    @FXML
    private TableColumn<BookIssue, LocalDate> issueDateColumn;

    @FXML
    private TableColumn<BookIssue, LocalDate> dueDateColumn;

    @FXML
    private TableColumn<BookIssue, String> statusColumn;

    @FXML
    private Label totalIssuedLabel;

    @FXML
    private Label borrowedLabel;

    @FXML
    private Label overdueLabel;

    @FXML
    private Label returnedLabel;

    @FXML
    public IssueReturnController issueReturnController = new IssueReturnController();

    @FXML
    public void initialize() {

        memberComboBox.setItems(FXCollections.observableArrayList(new MemberDAO().getAllMembers()));
        memberComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Member member) {
                if (member == null) {
                    return "";
                }
                return member.getMemberId() + " - " + member.getName();
            }

            @Override
            public Member fromString(String s) {
                return null;
            }
        });

        bookComboBox.setItems(FXCollections.observableArrayList(new BookDAO().getAllBooks()));
        bookComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Book book) {
                if (book == null) {
                    return "";
                }
                return book.getIsbn() + " - " + book.getTitle() + " (Available: " + book.getQuantity() + ")";
            }

            @Override
            public Book fromString(String s) {
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
            BookIssue issue = cellData.getValue();
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

        issueReturnController.loadIssuedBooks(issuedBooksTable);
        issueReturnController.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void showDashboardOnAction() {
        issueReturnController.loadPage("/edu/icet/view/Dashboard.fxml", memberComboBox);
    }

    @FXML
    public void showBooksOnAction() {
        issueReturnController.loadPage("/edu/icet/view/Books.fxml", memberComboBox);
    }

    @FXML
    public void showMembersOnAction() {
        issueReturnController.loadPage("/edu/icet/view/Members.fxml", memberComboBox);
    }

    @FXML
    public void logoutOnAction() {
        issueReturnController.loadPage("/edu/icet/view/LoginPage.fxml", memberComboBox);
    }

    @FXML
    public void issueBookOnAction() {
        issueReturnController.issuedTheBook(memberComboBox, bookComboBox, issueDatePicker, dueDatePicker, issuedBooksTable);
        issueReturnController.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void returnBookOnAction() {
        issueReturnController.returnBook(issuedBooksTable, bookComboBox, memberComboBox);
        issueReturnController.updateStatCards(totalIssuedLabel, borrowedLabel, returnedLabel, overdueLabel);
    }

    @FXML
    public void showOverdueBooksOnAction() {
        issueReturnController.showOverdueBooks(issuedBooksTable);
    }

    @FXML
    public void showAllIssuesOnAction() {
        issueReturnController.loadIssuedBooks(issuedBooksTable);
    }
}
