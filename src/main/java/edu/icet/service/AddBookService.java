package edu.icet.service;

import edu.icet.model.dto.BookDTO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public interface AddBookService {

    void validateBookDetails(TextField isbnField, TextField titleField, TextField authorField, TextField categoryField,
                             TextField quantityField);

    void closedTheAddBookForm(TextField isbnField);

    void setBookToEdit(TextField isbnField, TextField titleField, TextField authorField, TextField categoryField,
                       TextField quantityField, BookDTO bookDTO, Button saveButton, Label titleLabel);

    void setExistingBooks(ObservableList<BookDTO> booksList);

    boolean isDuplicateIsbn(String isbn);

    void showAlert(Alert.AlertType alertType, String info, String message);

    BookDTO getNewBook();
}


