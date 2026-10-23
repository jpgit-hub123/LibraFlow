package edu.icet.controller;

import edu.icet.dao.BookDAO;
import edu.icet.dao.MemberDAO;
import edu.icet.model.Book;
import edu.icet.model.Member;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    public void issueBookOnAction(ActionEvent actionEvent) {
        issueReturnController.issuedTheBook(memberComboBox, bookComboBox, issueDatePicker, dueDatePicker);
    }
}
