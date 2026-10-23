package edu.icet.controller;

import edu.icet.dao.BookDAO;
import edu.icet.dao.BookIssueDAO;
import edu.icet.dao.MemberDAO;
import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import edu.icet.model.Member;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class IssueReturnController {


    public void loadPage(String fxmlFile, ComboBox<Member> memberComboBox) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Parent rootNode = loader.load();
            memberComboBox.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAlert(Alert.AlertType alertType, String info, String message) {

        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public void issuedTheBook(ComboBox<Member> memberComboBox, ComboBox<Book> bookComboBox, DatePicker issueDatePicker,
                              DatePicker dueDatePicker, TableView<BookIssue> issuedBooksTable) {

        Member member = memberComboBox.getValue();
        Book book = bookComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (member == null || book == null || issueDate == null || dueDate == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No required details selected!",
                    "Please fill details in all fields."
            );
            return;
        }

        if (dueDate.isBefore(issueDate)) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error!",
                    "Due date cannot be before the issue date."
            );
            return;
        }

        if (book.getQuantity() == 0) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unavailable!",
                    "This book is currently out of stock."
            );
            return;
        }

        BookIssue bookIssue = new BookIssue(member.getMemberId(), book.getIsbn(), issueDate, dueDate);

        BookIssueDAO bookIssueDAO = new BookIssueDAO();

        if (bookIssueDAO.issueBook(bookIssue)) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book issued successfully."
            );
            memberComboBox.getSelectionModel().clearSelection();
            bookComboBox.getSelectionModel().clearSelection();
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
            bookComboBox.setItems(FXCollections.observableArrayList(new BookDAO().getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(new MemberDAO().getAllMembers()));
            loadIssuedBooks(issuedBooksTable);
        } else {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unsuccess!",
                    "Book not issued successfully, Try again."
            );
        }
    }

    public void loadIssuedBooks(TableView<BookIssue> issuedBooksTable) {
        BookIssueDAO bookIssueDAO = new BookIssueDAO();
        issuedBooksTable.setItems(FXCollections.observableArrayList(bookIssueDAO.getAllIssues()));
    }

    public void returnBook(TableView<BookIssue> issuedBooksTable, ComboBox<Book> bookComboBox, ComboBox<Member> memberComboBox) {

        BookIssue selectedIssue = issuedBooksTable.getSelectionModel().getSelectedItem();

        if (selectedIssue == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No any issued book Selected!",
                    "Please select a book to return."
            );
            return;
        }

        if ("RETURNED".equals(selectedIssue.getStatus())) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Already Returned!",
                    "This book has already been returned."
            );
            return;
        }

        BookIssueDAO bookIssueDAO = new BookIssueDAO();

        if (bookIssueDAO.returnBook(selectedIssue.getIssueId())) {

            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book returned successfully."
            );

            loadIssuedBooks(issuedBooksTable);
            bookComboBox.setItems(FXCollections.observableArrayList(new BookDAO().getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(new MemberDAO().getAllMembers()));
        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Return Failed!",
                    "Unable to return this book. Please try again."
            );
        }
    }

    public void showOverdueBooks(TableView<BookIssue> issuedBooksTable) {
        BookIssueDAO bookIssueDAO = new BookIssueDAO();
        var overdueIssues = bookIssueDAO.getAllIssues()
                .stream()
                .filter(BookIssue::isOverdue)
                .toList();

        issuedBooksTable.setItems(
                FXCollections.observableArrayList(overdueIssues)
        );
    }

    public void updateStatCards(Label totalIssuedLabel, Label borrowedLabel, Label overdueLabel, Label returnedLabel) {
        BookIssueDAO bookIssueDAO = new BookIssueDAO();

        totalIssuedLabel.setText(
                String.valueOf(bookIssueDAO.getTotalIssuedBooks())
        );

        borrowedLabel.setText(
                String.valueOf(bookIssueDAO.getCurrentlyBorrowedBooks())
        );

        overdueLabel.setText(
                String.valueOf(bookIssueDAO.getOverdueBooks())
        );

        returnedLabel.setText(
                String.valueOf(bookIssueDAO.getReturnedBooks())
        );
    }
}
