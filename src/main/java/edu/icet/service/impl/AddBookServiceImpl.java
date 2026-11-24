package edu.icet.service.impl;

import edu.icet.mapper.BookMapper;
import edu.icet.model.dto.BookDTO;
import edu.icet.repository.AddBookRepository;
import edu.icet.repository.impl.AddBookRepositoryImpl;
import edu.icet.service.AddBookService;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookServiceImpl implements AddBookService {

    private ObservableList<BookDTO> existingBookDTOs;
    private final BookMapper bookMapper = new BookMapper();
    private BookDTO newBookDTO;
    private BookDTO bookDtoToEdit;
    private final AddBookRepository addBookRepository = new AddBookRepositoryImpl();

    @Override
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

        if (bookDtoToEdit == null) {
            if (isDuplicateIsbn(isbn)){
                return;
            }
            BookDTO bookDTO = new BookDTO(isbn, title, author, category, quantity);

            if (addBookRepository.saveBook(bookMapper.toEntity(bookDTO))) {
                newBookDTO = bookDTO;
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Failed to save the book. Please try again."
                );
                return;
            }
        } else {

            String originalIsbnOfEditedBook = bookDtoToEdit.getIsbn();

            if (isDuplicateIsbn(isbn)) {
                return;
            }

            BookDTO updatedBookDTO = new BookDTO(isbn, title, author, category, quantity);

            if (addBookRepository.updateBook(originalIsbnOfEditedBook, bookMapper.toEntity(updatedBookDTO))) {
                bookDtoToEdit.setIsbn(isbn);
                bookDtoToEdit.setTitle(title);
                bookDtoToEdit.setAuthor(author);
                bookDtoToEdit.setCategory(category);
                bookDtoToEdit.setQuantity(quantity);
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

    @Override
    public boolean isDuplicateIsbn(String isbn) {
        for (BookDTO bookDTO : existingBookDTOs) {
            if (bookDTO.getIsbn().equalsIgnoreCase(isbn)) {
                if (bookDtoToEdit != null && bookDTO.getIsbn().equalsIgnoreCase(bookDtoToEdit.getIsbn())) {
                    continue;
                }
            }
            if (bookDTO.getIsbn().equalsIgnoreCase(isbn)) {
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

    @Override
    public void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public BookDTO getNewBook() {
        return newBookDTO;
    }

    @Override
    public void closedTheAddBookForm(TextField isbnField) {
        Stage addBookWindow = (Stage) isbnField.getScene().getWindow();
        addBookWindow.close();
    }

    @Override
    public void setExistingBooks(ObservableList<BookDTO> booksList) {
        this.existingBookDTOs = booksList;
    }

    @Override
    public void setBookToEdit(TextField isbnField, TextField titleField, TextField authorField,
                              TextField categoryField, TextField quantityField, BookDTO bookDTO,
                              Button saveButton, Label titleLabel) {

        this.bookDtoToEdit = bookDTO;

        isbnField.setText(bookDTO.getIsbn());
        titleField.setText(bookDTO.getTitle());
        authorField.setText(bookDTO.getAuthor());
        categoryField.setText(bookDTO.getCategory());
        quantityField.setText(String.valueOf(bookDTO.getQuantity()));

        saveButton.setText("Update");
        titleLabel.setText("Update Book");
    }
}
