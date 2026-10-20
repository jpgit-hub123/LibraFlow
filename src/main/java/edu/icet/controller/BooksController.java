package edu.icet.controller;

import edu.icet.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksController {

    private ObservableList<Book> booksList;

    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            Parent rootNode = loader.load();

            searchField.getScene().setRoot(rootNode);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

    public void setBooksDataIntoTable(TableColumn<Book, String> isbnColumn, TableColumn<Book, String> titleColumn,
                                      TableColumn<Book, String> authorColumn, TableColumn<Book, String> categoryColumn,
                                      TableColumn<Book, Integer> quantityColumn, TableView<Book> booksTable) {

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        booksList = FXCollections.observableArrayList(
                new Book("9780132350884", "Clean Code", "Robert C. Martin", "Programming", 5),
                new Book("9780134685991", "Effective Java", "Joshua Bloch", "Programming", 3),
                new Book("9780596009205", "Head First Java", "Kathy Sierra", "Programming", 7)
        );

        booksTable.setItems(booksList);
    }

    public void getRelatedBooks(TextField searchField, TableView<Book> booksTable) {

        String searchWord = searchField.getText().trim().toLowerCase();
        ObservableList<Book> filterBooks = FXCollections.observableArrayList();

        for (Book book : booksList) {

            if (book.getIsbn().toLowerCase().contains(searchWord)
                || book.getTitle().toLowerCase().contains(searchWord)
                || book.getAuthor().toLowerCase().contains(searchWord)
                || book.getCategory().toLowerCase().contains(searchWord)) {

                filterBooks.add(book);
            }
        }
        booksTable.setItems(filterBooks);
    }

    public void loadForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddBook.fxml"));

            Parent form = loader.load();

            AddBookPageController addBookPageController = loader.getController();
            addBookPageController.addBookController.setExistingBooks(booksList);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Add New Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);

            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            Book newBook = addBookPageController.addBookController.getNewBook();

            if (newBook != null) {
                booksList.add(newBook);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editTheSelectedBook(TableView<Book> booksTable) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Book Selected",
                    "Please select a book to edit."
            );
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddBook.fxml"));
            Parent form = loader.load();
            AddBookPageController addBookPageController = loader.getController();
            addBookPageController.toEditBook(selectedBook);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Edit Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            booksTable.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String info, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(info);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
