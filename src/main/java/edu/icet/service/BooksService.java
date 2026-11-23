package edu.icet.service;

import edu.icet.model.entity.BookEntity;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public interface BooksService {

    void loadPage(String fxmlFile, TextField searchField);

    void setBooksDataIntoTable(TableColumn<BookEntity, String> isbnColumn, TableColumn<BookEntity, String> titleColumn,
                               TableColumn<BookEntity, String> authorColumn, TableColumn<BookEntity, String> categoryColumn,
                               TableColumn<BookEntity, Integer> quantityColumn, TableView<BookEntity> booksTable);

    void getRelatedBooks(TextField searchField, TableView<BookEntity> booksTable);

    void loadForm(TextField searchField, TableView<BookEntity> booksTable);

    void editTheSelectedBook(TextField searchField, TableView<BookEntity> booksTable);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void deleteSelectedBook(TextField searchField, TableView<BookEntity> booksTable);
}
