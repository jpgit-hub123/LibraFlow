package edu.icet.service;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.dto.MemberDTO;
import javafx.scene.control.*;

import java.util.List;

public interface IssueReturnService {

    void loadIssuedBooks(TableView<BookIssueDTO> issuedBooksTable);

    void updateStatCards(Label totalIssuedLabel, Label borrowedLabel, Label returnedLabel, Label overdueLabel);

    void loadPage(String s, ComboBox<MemberDTO> memberComboBox);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void issuedTheBook(ComboBox<MemberDTO> memberComboBox, ComboBox<BookDTO> bookComboBox, DatePicker issueDatePicker, DatePicker dueDatePicker, TableView<BookIssueDTO> issuedBooksTable);

    void returnBook(TableView<BookIssueDTO> issuedBooksTable, ComboBox<BookDTO> bookComboBox, ComboBox<MemberDTO> memberComboBox);

    void showOverdueBooks(TableView<BookIssueDTO> issuedBooksTable);

    List<BookDTO> getAllBooks();

    List<MemberDTO> getAllMembers();
}




