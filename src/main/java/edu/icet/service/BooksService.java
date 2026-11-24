package edu.icet.service;

import edu.icet.model.dto.BookDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public interface BooksService {

    void loadPage(String fxmlFile, TextField searchField);

    void setBooksDataIntoTable(TableColumn<BookDTO, String> isbnColumn, TableColumn<BookDTO, String> titleColumn,
                               TableColumn<BookDTO, String> authorColumn, TableColumn<BookDTO, String> categoryColumn,
                               TableColumn<BookDTO, Integer> quantityColumn, TableView<BookDTO> booksTable);

    void getRelatedBooks(TextField searchField, TableView<BookDTO> booksTable);

    void loadForm(TextField searchField, TableView<BookDTO> booksTable);

    void editTheSelectedBook(TextField searchField, TableView<BookDTO> booksTable);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void deleteSelectedBook(TextField searchField, TableView<BookDTO> booksTable);
}
