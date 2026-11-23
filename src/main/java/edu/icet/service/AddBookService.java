package edu.icet.service;

import edu.icet.model.entity.BookEntity;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public interface AddBookService {

    void validateBookDetails(TextField isbnField, TextField titleField, TextField authorField, TextField categoryField,
                             TextField quantityField);


    void closedTheAddBookForm(TextField isbnField);

    void setBookToEdit(TextField isbnField, TextField titleField, TextField authorField, TextField categoryField,
                       TextField quantityField, BookEntity bookEntity, Button saveButton, Label titleLabel);

    void setExistingBooks(ObservableList<BookEntity> booksList);

    BookEntity getNewBook();
}

