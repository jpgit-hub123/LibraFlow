package edu.icet.controller;

import edu.icet.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BooksPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> categoryColumn;

    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    public BooksController booksController = new BooksController();

    private ObservableList<Book> books;

    @FXML
    private void showDashboardOnAction() {
        booksController.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    @FXML
    private void showAddBookFormOnAction() {
        System.out.println("Add Book button clicked!");
    }

    @FXML
    public void initialize() {
        booksController.setBooksDataIntoTable(isbnColumn, titleColumn, authorColumn, categoryColumn, quantityColumn, booksTable);
    }

    @FXML
    private void searchBooks() {
        booksController.getRelatedBooks(searchField, booksTable);
    }
}


