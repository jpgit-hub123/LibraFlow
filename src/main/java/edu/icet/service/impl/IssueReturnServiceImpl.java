package edu.icet.service.impl;

import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import edu.icet.repository.IssueReturnRepository;
import edu.icet.repository.impl.IssueReturnRepositoryImpl;
import edu.icet.service.IssueReturnService;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class IssueReturnServiceImpl implements IssueReturnService {

    private final IssueReturnRepository issueReturnRepository = new IssueReturnRepositoryImpl();

    @Override
    public void loadPage(String fxmlFile, ComboBox<MemberEntity> memberComboBox) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Parent rootNode = loader.load();
            memberComboBox.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
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

        if (issueReturnRepository.issueBook(bookIssueEntity)) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book issued successfully."
            );
            memberComboBox.getSelectionModel().clearSelection();
            bookComboBox.getSelectionModel().clearSelection();
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
            bookComboBox.setItems(FXCollections.observableArrayList(issueReturnRepository.getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(issueReturnRepository.getAllMembers()));
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
        issuedBooksTable.setItems(FXCollections.observableArrayList(issueReturnRepository.getAllIssues()));
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

        if (issueReturnRepository.returnBook(selectedIssue.getIssueId())) {

            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book returned successfully."
            );

            loadIssuedBooks(issuedBooksTable);
            bookComboBox.setItems(FXCollections.observableArrayList(issueReturnRepository.getAllBooks()));
            memberComboBox.setItems(FXCollections.observableArrayList(issueReturnRepository.getAllMembers()));
        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Return Failed!",
                    "Unable to return this book. Please try again."
            );
        }
    }

    public void showOverdueBooks(TableView<BookIssueEntity> issuedBooksTable) {
        List<BookIssueEntity> overdueIssues = issueReturnRepository.getAllIssues()
                .stream()
                .filter(BookIssueEntity::isOverdue)
                .toList();

        issuedBooksTable.setItems(
                FXCollections.observableArrayList(overdueIssues)
        );
    }

    @Override
    public List<BookEntity> getAllBooks() {
        return issueReturnRepository.getAllBooks();
    }

    public void updateStatCards(Label totalIssuedLabel, Label borrowedLabel, Label returnedLabel, Label overdueLabel) {

        totalIssuedLabel.setText(
                String.valueOf(issueReturnRepository.getTotalIssuedBooks())
        );

        borrowedLabel.setText(
                String.valueOf(issueReturnRepository.getCurrentlyBorrowedBooks())
        );

        overdueLabel.setText(
                String.valueOf(issueReturnRepository.getOverdueBooks())
        );

        returnedLabel.setText(
                String.valueOf(issueReturnRepository.getReturnedBooks())
        );
    }
}
