package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.entity.BookEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<BookEntity> booksList;
    private final BookDTO bookDTO = new BookDTO();

    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            Parent rootNode = loader.load();

            searchField.getScene().setRoot(rootNode);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

    public void setBooksDataIntoTable(TableColumn<BookEntity, String> isbnColumn, TableColumn<BookEntity, String> titleColumn,
                                      TableColumn<BookEntity, String> authorColumn, TableColumn<BookEntity, String> categoryColumn,
                                      TableColumn<BookEntity, Integer> quantityColumn, TableView<BookEntity> booksTable) {

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        booksList = FXCollections.observableArrayList(bookDTO.getAllBooks());
        booksTable.setItems(booksList);
    }

    public void getRelatedBooks(TextField searchField, TableView<BookEntity> booksTable) {

        String searchWord = searchField.getText().trim().toLowerCase();
        ObservableList<BookEntity> filterBookEntities = FXCollections.observableArrayList();

        for (BookEntity bookEntity : booksList) {

            if (bookEntity.getIsbn().toLowerCase().contains(searchWord)
                || bookEntity.getTitle().toLowerCase().contains(searchWord)
                || bookEntity.getAuthor().toLowerCase().contains(searchWord)
                || bookEntity.getCategory().toLowerCase().contains(searchWord)) {

                filterBookEntities.add(bookEntity);
            }
        }
        booksTable.setItems(filterBookEntities);
    }

    public void loadForm(TextField searchField, TableView<BookEntity> booksTable) {
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

            BookEntity newBookEntity = addBookPageController.addBookController.getNewBook();

            if (newBookEntity != null) {
                booksList.add(newBookEntity);
                getRelatedBooks(searchField, booksTable);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editTheSelectedBook(TextField searchField, TableView<BookEntity> booksTable) {
        BookEntity selectedBookEntity = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBookEntity == null) {
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
            addBookPageController.addBookController.setExistingBooks(booksList);
            addBookPageController.toEditBook(selectedBookEntity);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Edit Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            booksList.setAll(bookDTO.getAllBooks());
            getRelatedBooks(searchField, booksTable);
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

    public void deleteSelectedBook(TextField searchField, TableView<BookEntity> booksTable) {
        BookEntity selectedBookEntity = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBookEntity == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Book Selected",
                    "Please select a book to delete."
            );
            return;
        }

        if (bookDTO.hasIssueHistory(selectedBookEntity.getBookId())) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Cannot Delete Book",
                    "This book cannot be deleted because it has transaction history."
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Book");
        confirmation.setHeaderText("Delete " + selectedBookEntity.getTitle() + "?");
        confirmation.setContentText("Are you sure you want to delete this book?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (bookDTO.deleteBook(selectedBookEntity.getIsbn())) {
                    booksList.remove(selectedBookEntity);
                    getRelatedBooks(searchField, booksTable);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Book Deleted",
                            "The book was deleted successfully."
                    );
                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Database Error",
                            "Failed to delete the book from the database."
                    );
                }

            }
        });
    }
}
