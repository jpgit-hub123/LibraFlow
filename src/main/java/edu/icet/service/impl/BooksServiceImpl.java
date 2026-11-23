package edu.icet.service.impl;

import edu.icet.controller.AddBookPageController;
import edu.icet.model.entity.BookEntity;
import edu.icet.repository.impl.BooksRepositoryImpl;
import edu.icet.service.BooksService;
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

public class BooksServiceImpl implements BooksService {

    private ObservableList<BookEntity> booksList;

    private final BooksRepositoryImpl booksRepository = new BooksRepositoryImpl();

    @Override
    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent rootNode = loader.load();
            searchField.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBooksDataIntoTable(TableColumn<BookEntity, String> isbnColumn, TableColumn<BookEntity, String> titleColumn,
                                      TableColumn<BookEntity, String> authorColumn, TableColumn<BookEntity, String> categoryColumn,
                                      TableColumn<BookEntity, Integer> quantityColumn, TableView<BookEntity> booksTable) {

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        booksList = FXCollections.observableArrayList(booksRepository.getAllBooks());
        booksTable.setItems(booksList);
    }

    @Override
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

    @Override
    public void loadForm(TextField searchField, TableView<BookEntity> booksTable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/icet/view/AddBook.fxml"));
            Parent form = loader.load();

            AddBookPageController addBookPageController = loader.getController();
            addBookPageController.addBookService.setExistingBooks(booksList);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Add New Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);

            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            BookEntity newBookEntity = addBookPageController.addBookService.getNewBook();

            if (newBookEntity != null) {
                booksList.add(newBookEntity);
                getRelatedBooks(searchField, booksTable);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
            addBookPageController.addBookService.setExistingBooks(booksList);
            addBookPageController.toEditBook(selectedBookEntity);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Edit Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            booksList.setAll(booksRepository.getAllBooks());
            getRelatedBooks(searchField, booksTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        if (booksRepository.hasIssueHistory(selectedBookEntity.getBookId())) {
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
                if (booksRepository.deleteBook(selectedBookEntity.getIsbn())) {
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
