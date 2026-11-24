package edu.icet.service;

import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import javafx.scene.control.*;

import java.util.List;

public interface IssueReturnService {


    void loadIssuedBooks(TableView<BookIssueEntity> issuedBooksTable);

    void updateStatCards(Label totalIssuedLabel, Label borrowedLabel, Label returnedLabel, Label overdueLabel);

    void loadPage(String s, ComboBox<MemberEntity> memberComboBox);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void issuedTheBook(ComboBox<MemberEntity> memberComboBox, ComboBox<BookEntity> bookComboBox, DatePicker issueDatePicker, DatePicker dueDatePicker, TableView<BookIssueEntity> issuedBooksTable);

    void returnBook(TableView<BookIssueEntity> issuedBooksTable, ComboBox<BookEntity> bookComboBox, ComboBox<MemberEntity> memberComboBox);

    void showOverdueBooks(TableView<BookIssueEntity> issuedBooksTable);

    List<BookEntity> getAllBooks();
}


