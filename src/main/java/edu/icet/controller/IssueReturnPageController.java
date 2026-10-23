package edu.icet.controller;

import edu.icet.dao.BookDAO;
import edu.icet.dao.MemberDAO;
import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import edu.icet.model.Member;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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

    public IssueReturnController issueReturnController = new IssueReturnController();

    @FXML
    public void initialize() {
        MemberDAO memberDAO = new MemberDAO();
        memberComboBox.setItems(FXCollections.observableArrayList(memberDAO.getAllMembers()));

        memberComboBox.setConverter(new StringConverter<Member>() {
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

        BookDAO bookDAO = new BookDAO();
        bookComboBox.setItems(FXCollections.observableArrayList(bookDAO.getAllBooks()));

        bookComboBox.setConverter(new StringConverter<Book>() {
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

        issueReturnController.loadIssuedBooks(issuedBooksTable);
    }

    public void showDashboardOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Dashboard.fxml", memberComboBox);
    }

    public void showBooksOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Books.fxml", memberComboBox);
    }

    public void showMembersOnAction(ActionEvent actionEvent) {
        issueReturnController.loadPage("/edu/icet/view/Members.fxml", memberComboBox);
    }

    public void issueBookOnAction(ActionEvent actionEvent) {
        issueReturnController.issuedTheBook(memberComboBox, bookComboBox, issueDatePicker, dueDatePicker, issuedBooksTable);
    }

    public void returnBookOnAction(ActionEvent actionEvent) {
        issueReturnController.returnBook(issuedBooksTable, bookComboBox, memberComboBox);
    }

    public void showOverdueBooksOnAction(ActionEvent actionEvent) {
        issueReturnController.showOverdueBooks(issuedBooksTable);
    }

    public void showAllIssuesOnAction(ActionEvent actionEvent) {
        issueReturnController.loadIssuedBooks(issuedBooksTable);
    }
}
