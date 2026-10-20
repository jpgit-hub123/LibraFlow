package edu.icet.controller;

import edu.icet.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddBookPageController {

    @FXML
    public Button saveButton;

    @FXML
    public Label titleLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField quantityField;

    public AddBookController addBookController = new AddBookController();

    @FXML
    private void saveBookOnAction() {
        addBookController.validateBookDetails(isbnField, titleField, authorField, categoryField, quantityField);
    }
    @FXML
    private void cancelFormOnAction() {
        addBookController.closedTheAddBookForm(isbnField);
    }

    public void toEditBook(Book book){
        addBookController.setBookToEdit(isbnField, titleField, authorField, categoryField, quantityField, book,
                saveButton, titleLabel);
    }
}
