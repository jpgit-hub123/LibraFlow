package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class IssueReturnController {

    BookIssueDTO bookIssueDTO = new BookIssueDTO();

    public void loadPage(String fxmlFile, ComboBox<MemberEntity> memberComboBox) {

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

    public void issuedTheBook(ComboBox<MemberEntity> memberComboBox, ComboBox<BookEntity> bookComboBox, DatePicker issueDatePicker,
                              DatePicker dueDatePicker, TableView<BookIssueEntity> issuedBooksTable) {

        MemberEntity memberEntity = memberComboBox.getValue();
        BookEntity bookEntity = bookComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (memberEntity == null || bookEntity == null || issueDate == null || dueDate == null) {
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

        if (bookEntity.getQuantity() == 0) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unavailable!",
                    "This book is currently out of stock."
            );
            return;
        }

        BookIssueEntity bookIssueEntity = new BookIssueEntity(memberEntity.getMemberId(), bookEntity, issueDate, dueDate);

        if (bookIssueDTO.issueBook(bookIssueEntity)) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book issued successfully."
            );
            memberComboBox.getSelectionModel().clearSelection();
            bookComboBox.getSelectionModel().clearSelection();
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
            bookComboBox.setItems(FXCollections.observableArrayList(new BookDTO().getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(new MemberDTO().getAllMembers()));
            loadIssuedBooks(issuedBooksTable);
        } else {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unsuccess!",
                    "Book not issued successfully, Try again."
            );
        }

    }

    public void loadIssuedBooks(TableView<BookIssueEntity> issuedBooksTable) {
        issuedBooksTable.setItems(FXCollections.observableArrayList(bookIssueDTO.getAllIssues()));
    }

    public void returnBook(TableView<BookIssueEntity> issuedBooksTable, ComboBox<BookEntity> bookComboBox, ComboBox<MemberEntity> memberComboBox) {

        BookIssueEntity selectedIssue = issuedBooksTable.getSelectionModel().getSelectedItem();

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

        if (bookIssueDTO.returnBook(selectedIssue.getIssueId())) {

            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book returned successfully."
            );

            loadIssuedBooks(issuedBooksTable);
            bookComboBox.setItems(FXCollections.observableArrayList(new BookDTO().getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(new MemberDTO().getAllMembers()));
        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Return Failed!",
                    "Unable to return this book. Please try again."
            );
        }
    }

    public void showOverdueBooks(TableView<BookIssueEntity> issuedBooksTable) {
        var overdueIssues = bookIssueDTO.getAllIssues()
                .stream()
                .filter(BookIssueEntity::isOverdue)
                .toList();

        issuedBooksTable.setItems(
                FXCollections.observableArrayList(overdueIssues)
        );
    }

    public void updateStatCards(Label totalIssuedLabel, Label borrowedLabel, Label returnedLabel, Label overdueLabel) {

        totalIssuedLabel.setText(
                String.valueOf(bookIssueDTO.getTotalIssuedBooks())
        );

        borrowedLabel.setText(
                String.valueOf(bookIssueDTO.getCurrentlyBorrowedBooks())
        );

        overdueLabel.setText(
                String.valueOf(bookIssueDTO.getOverdueBooks())
        );

        returnedLabel.setText(
                String.valueOf(bookIssueDTO.getReturnedBooks())
        );
    }
}
