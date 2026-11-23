package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.entity.BookEntity;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {

    private final BookDTO bookDTO = new BookDTO();
    private ObservableList<BookEntity> existingBookEntities;
    private BookEntity newBookEntity;
    private BookEntity bookEntityToEdit;

    public void validateBookDetails(TextField isbnField, TextField titleField, TextField authorField,
                                    TextField categoryField, TextField quantityField) {

        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityTxt = quantityField.getText().trim();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty() || quantityTxt.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please fill in all fields."
            );
            return;
        }

        if (isbn.length() > 20 || title.length() > 150 || author.length() > 100 || category.length() > 100) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    """
                            ISBN must not exceed 20 characters.
                            Title must not exceed 150 characters.
                            Author must not exceed 100 characters.
                            Category must not exceed 100 characters."""
            );
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityTxt);
            if (quantity < 0) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Quantity cannot be negative."
                );
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Quantity",
                    "Please enter a valid whole number for quantity."
            );
            return;
        }

        if (bookEntityToEdit == null) {
            if (isDuplicateIsbn(isbn)){
                return;
            }
            BookEntity bookEntity = new BookEntity(isbn, title, author, category, quantity);

            if (bookDTO.saveBook(bookEntity)) {
                newBookEntity = bookEntity;
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to save the book. Please try again."
                );
                return;
            }
        } else {

            String originalIsbnOfEditedBook = bookEntityToEdit.getIsbn();

            if (isDuplicateIsbn(isbn)) {
                return;
            }

            BookEntity updatedBookEntity = new BookEntity(isbn, title, author, category, quantity);

            if (bookDTO.updateBook(originalIsbnOfEditedBook, updatedBookEntity)) {
                bookEntityToEdit.setIsbn(isbn);
                bookEntityToEdit.setTitle(title);
                bookEntityToEdit.setAuthor(author);
                bookEntityToEdit.setCategory(category);
                bookEntityToEdit.setQuantity(quantity);
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to update the book. Please try again."
                );
                return;
            }
        }

        Stage stage = (Stage) isbnField.getScene().getWindow();
        stage.close();
    }

    private boolean isDuplicateIsbn(String isbn) {
        for (BookEntity bookEntity : existingBookEntities) {
            if (bookEntity.getIsbn().equalsIgnoreCase(isbn)) {
                if (bookEntityToEdit != null && bookEntity.getIsbn().equalsIgnoreCase(bookEntityToEdit.getIsbn())) {
                    continue;
                }
            }
            if (bookEntity.getIsbn().equalsIgnoreCase(isbn)) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicate ISBN",
                        "A book with this ISBN already exists."
                );
                return true;
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BookEntity getNewBook() {
        return newBookEntity;
    }

    public void closedTheAddBookForm(TextField isbnField) {
        Stage addBookWindow = (Stage) isbnField.getScene().getWindow();
        addBookWindow.close();
    }

    public void setExistingBooks(ObservableList<BookEntity> booksList) {
        this.existingBookEntities = booksList;
    }

    public void setBookToEdit(TextField isbnField, TextField titleField, TextField authorField,
                              TextField categoryField, TextField quantityField, BookEntity bookEntity,
                              Button saveButton, Label titleLabel) {

        this.bookEntityToEdit = bookEntity;

        isbnField.setText(bookEntity.getIsbn());
        titleField.setText(bookEntity.getTitle());
        authorField.setText(bookEntity.getAuthor());
        categoryField.setText(bookEntity.getCategory());
        quantityField.setText(String.valueOf(bookEntity.getQuantity()));

        saveButton.setText("Update");
        titleLabel.setText("Update Book");
    }
}
