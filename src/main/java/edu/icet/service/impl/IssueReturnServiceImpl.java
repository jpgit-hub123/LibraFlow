package edu.icet.service.impl;

import edu.icet.mapper.BookMapper;
import edu.icet.mapper.IssueReturnMapper;
import edu.icet.mapper.MemberMapper;
import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.dto.MemberDTO;
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
    public void loadPage(String fxmlFile, ComboBox<MemberDTO> memberComboBox) {
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
    public void issuedTheBook(ComboBox<MemberDTO> memberComboBox, ComboBox<BookDTO> bookComboBox, DatePicker issueDatePicker,
                              DatePicker dueDatePicker, TableView<BookIssueDTO> issuedBooksTable) {

        MemberDTO memberDTO = memberComboBox.getValue();
        BookDTO bookDTO = bookComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (memberDTO == null || bookDTO == null || issueDate == null || dueDate == null) {
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

        if (bookDTO.getQuantity() == 0) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unavailable!",
                    "This book is currently out of stock."
            );
            return;
        }

        BookIssueDTO bookIssueDTO = new BookIssueDTO();

        bookIssueDTO.setMemberId(memberDTO.getMemberId());
        bookIssueDTO.setBookId(bookDTO.getBookId());
        bookIssueDTO.setIsbn(bookDTO.getIsbn());
        bookIssueDTO.setBookTitle(bookDTO.getTitle());
        bookIssueDTO.setIssueDate(issueDate);
        bookIssueDTO.setDueDate(dueDate);

        IssueReturnMapper issueReturnMapper = new IssueReturnMapper();
        MemberMapper memberMapper = new MemberMapper();
        BookMapper bookMapper = new BookMapper();

        if (issueReturnRepository.issueBook(issueReturnMapper.toEntity(bookIssueDTO, bookMapper.toEntity(bookDTO)))) {
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book issued successfully."
            );
            memberComboBox.getSelectionModel().clearSelection();
            bookComboBox.getSelectionModel().clearSelection();
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
            bookComboBox.setItems(FXCollections.observableArrayList(
                    issueReturnRepository.getAllBooks().stream().map(bookMapper::toDTO).toList())
            );
            memberComboBox.setItems(FXCollections.observableArrayList(
                    issueReturnRepository.getAllMembers().stream().map(memberMapper::toDTO).toList()));
            loadIssuedBooks(issuedBooksTable);
        } else {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Unsuccess!",
                    "Book not issued successfully, Try again."
            );
        }

    }

    @Override
    public void loadIssuedBooks(TableView<BookIssueDTO> issuedBooksTable) {
        IssueReturnMapper issueReturnMapper = new IssueReturnMapper();
        issuedBooksTable.setItems(FXCollections.observableArrayList(
                issueReturnRepository.getAllIssues().stream().map(issueReturnMapper::toDTO).toList())
        );
    }

    @Override
    public void returnBook(TableView<BookIssueDTO> issuedBooksTable, ComboBox<BookDTO> bookComboBox, ComboBox<MemberDTO> memberComboBox) {

        BookIssueDTO selectedIssue = issuedBooksTable.getSelectionModel().getSelectedItem();

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

        MemberMapper memberMapper = new MemberMapper();
        BookMapper bookMapper = new BookMapper();

        if (issueReturnRepository.returnBook(selectedIssue.getIssueId())) {

            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Success!",
                    "Book returned successfully."
            );

            loadIssuedBooks(issuedBooksTable);
            bookComboBox.setItems(FXCollections.observableArrayList(
                    issueReturnRepository.getAllBooks().stream().map(bookMapper::toDTO).toList())
            );
            memberComboBox.setItems(FXCollections.observableArrayList(
                    issueReturnRepository.getAllMembers().stream().map(memberMapper::toDTO).toList())
            );
        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Return Failed!",
                    "Unable to return this book. Please try again."
            );
        }
    }

    @Override
    public void showOverdueBooks(TableView<BookIssueDTO> issuedBooksTable) {
        IssueReturnMapper issueReturnMapper = new IssueReturnMapper();
        final List<BookIssueDTO> dtoList = issueReturnRepository.getAllIssues().stream().map(issueReturnMapper::toDTO).toList();
        final List<BookIssueDTO> overdueDtoList = dtoList.stream().filter(BookIssueDTO::isOverdue).toList();
        issuedBooksTable.setItems(FXCollections.observableArrayList(overdueDtoList));
    }

    @Override
    public List<BookDTO> getAllBooks() {
        BookMapper bookMapper = new BookMapper();
        return issueReturnRepository.getAllBooks().stream().map(bookMapper::toDTO).toList();
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        MemberMapper memberMapper = new MemberMapper();
        return issueReturnRepository.getAllMembers().stream().map(memberMapper::toDTO).toList();
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
