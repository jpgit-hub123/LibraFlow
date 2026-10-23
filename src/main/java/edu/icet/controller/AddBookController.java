package edu.icet.controller;
import edu.icet.dao.BookDAO;
import edu.icet.model.Book;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {

    private final BookDAO bookDAO = new BookDAO();
    private ObservableList<Book> existingBooks;
    private Book newBook;
    private Book bookToEdit;

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
                    "ISBN must not exceed 20 characters.\n"
                            + "Title must not exceed 150 characters.\n"
                            + "Author must not exceed 100 characters.\n"
                            + "Category must not exceed 100 characters."
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

        if (bookToEdit == null) {
            if (isDuplicateIsbn(isbn)){
                return;
            }
            Book book = new Book(isbn, title, author, category, quantity);

            if (bookDAO.saveBook(book)) {
                newBook = book;
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to save the book. Please try again."
                );
                return;
            }
        } else {

            String originalIsbnOfEditedBook = bookToEdit.getIsbn();

            if (isDuplicateIsbn(isbn)) {
                return;
            }

            Book updatedBook = new Book(isbn, title, author, category, quantity);

            if (bookDAO.updateBook(originalIsbnOfEditedBook, updatedBook)) {
                bookToEdit.setIsbn(isbn);
                bookToEdit.setTitle(title);
                bookToEdit.setAuthor(author);
                bookToEdit.setCategory(category);
                bookToEdit.setQuantity(quantity);
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
        for (Book book : existingBooks) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                if (bookToEdit != null && book.getIsbn().equalsIgnoreCase(bookToEdit.getIsbn())) {
                    continue;
                }
            }
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
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

    public Book getNewBook() {
        return newBook;
    }

    public void closedTheAddBookForm(TextField isbnField) {
        Stage addBookWindow = (Stage) isbnField.getScene().getWindow();
        addBookWindow.close();
    }

    public void setExistingBooks(ObservableList<Book> booksList) {
        this.existingBooks = booksList;
    }

    public void setBookToEdit(TextField isbnField, TextField titleField, TextField authorField,
                              TextField categoryField, TextField quantityField, Book book,
                              Button saveButton, Label titleLabel) {

        this.bookToEdit = book;

        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        quantityField.setText(String.valueOf(book.getQuantity()));

        saveButton.setText("Update");
        titleLabel.setText("Update Book");
    }
}
