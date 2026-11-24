package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.service.AddBookService;
import edu.icet.service.impl.AddBookServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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

    @FXML
    public final AddBookService addBookService = new AddBookServiceImpl();

    @FXML
    private void saveBookOnAction() {
        addBookService.validateBookDetails(isbnField, titleField, authorField, categoryField, quantityField);
    }

    @FXML
    private void cancelFormOnAction() {
        addBookService.closedTheAddBookForm(isbnField);
    }

    @FXML
    public void initialize() {
        isbnField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 20 ? change : null
        ));

        titleField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 150 ? change : null
        ));

        authorField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 100 ? change : null
        ));

        categoryField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 100 ? change : null
        ));

        quantityField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    public void toEditBook(BookDTO bookDTO){
        addBookService.setBookToEdit(isbnField, titleField, authorField, categoryField, quantityField, bookDTO,
                saveButton, titleLabel);
    }
}
