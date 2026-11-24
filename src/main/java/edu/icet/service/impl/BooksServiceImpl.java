package edu.icet.service.impl;

import edu.icet.controller.AddBookPageController;
import edu.icet.mapper.BookMapper;
import edu.icet.model.dto.BookDTO;
import edu.icet.repository.BooksRepository;
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
import java.util.List;

public class BooksServiceImpl implements BooksService {

    private ObservableList<BookDTO> booksList;
    private final BookMapper bookMapper = new BookMapper();
    private final BooksRepository booksRepository = new BooksRepositoryImpl();

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
    public void setBooksDataIntoTable(TableColumn<BookDTO, String> isbnColumn, TableColumn<BookDTO, String> titleColumn,
                                      TableColumn<BookDTO, String> authorColumn, TableColumn<BookDTO, String> categoryColumn,
                                      TableColumn<BookDTO, Integer> quantityColumn, TableView<BookDTO> booksTable) {

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        List<BookDTO> bookDTOList = booksRepository.getAllBooks().stream().map(bookMapper::toDTO).toList();
        booksList = FXCollections.observableArrayList(bookDTOList);
        booksTable.setItems(booksList);
    }

    @Override
    public void getRelatedBooks(TextField searchField, TableView<BookDTO> booksTable) {

        String searchWord = searchField.getText().trim().toLowerCase();
        ObservableList<BookDTO> filteredBookDTOs = FXCollections.observableArrayList();

        for (BookDTO bookDTO : booksList) {
            if (bookDTO.getIsbn().toLowerCase().contains(searchWord)
                || bookDTO.getTitle().toLowerCase().contains(searchWord)
                || bookDTO.getAuthor().toLowerCase().contains(searchWord)
                || bookDTO.getCategory().toLowerCase().contains(searchWord)) {

                filteredBookDTOs.add(bookDTO);
            }
        }
        booksTable.setItems(filteredBookDTOs);
    }

    @Override
    public void loadForm(TextField searchField, TableView<BookDTO> booksTable) {
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

            BookDTO newBookDTO = addBookPageController.addBookService.getNewBook();

            if (newBookDTO != null) {
                booksList.add(newBookDTO);
                getRelatedBooks(searchField, booksTable);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editTheSelectedBook(TextField searchField, TableView<BookDTO> booksTable) {
        BookDTO selectedBookDTO = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBookDTO == null) {
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
            addBookPageController.toEditBook(selectedBookDTO);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("LibraFlow | Edit Book");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(form);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            booksList.setAll(booksRepository.getAllBooks().stream().map(bookMapper::toDTO).toList());
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
    public void deleteSelectedBook(TextField searchField, TableView<BookDTO> booksTable) {
        BookDTO selectedBooKDTO = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBooKDTO == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Book Selected",
                    "Please select a book to delete."
            );
            return;
        }

        if (booksRepository.hasIssueHistory(selectedBooKDTO.getBookId())) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Cannot Delete Book",
                    "This book cannot be deleted because it has transaction history."
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Book");
        confirmation.setHeaderText("Delete " + selectedBooKDTO.getTitle() + "?");
        confirmation.setContentText("Are you sure you want to delete this book?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                if (booksRepository.deleteBook(selectedBooKDTO.getBookId())) {
                    booksList.remove(selectedBooKDTO);
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
