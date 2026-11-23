package edu.icet.controller;

import edu.icet.model.entity.BookEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BooksPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BookEntity> booksTable;

    @FXML
    private TableColumn<BookEntity, String> isbnColumn;

    @FXML
    private TableColumn<BookEntity, String> titleColumn;

    @FXML
    private TableColumn<BookEntity, String> authorColumn;

    @FXML
    private TableColumn<BookEntity, String> categoryColumn;

    @FXML
    private TableColumn<BookEntity, Integer> quantityColumn;

    @FXML
    public BooksController booksController = new BooksController();

    @FXML
    private void showDashboardOnAction() {
        booksController.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    @FXML
    public void showMembersOnAction() {
        booksController.loadPage("/edu/icet/view/Members.fxml", searchField);
    }

    @FXML
    public void showIssueReturnOnAction() {
        booksController.loadPage("/edu/icet/view/IssueReturn.fxml", searchField);
    }

    @FXML
    public void logoutOnAction() {
        booksController.loadPage("/edu/icet/view/LoginPage.fxml", searchField);
    }

    @FXML
    private void showAddBookFormOnAction() {
        booksController.loadForm(searchField, booksTable);
    }

    @FXML
    public void initialize() {
        booksController.setBooksDataIntoTable(isbnColumn, titleColumn, authorColumn, categoryColumn, quantityColumn, booksTable);
    }

    @FXML
    public void searchBooks() {
        booksController.getRelatedBooks(searchField, booksTable);
    }

    @FXML
    public void editBookOnAction() {
        booksController.editTheSelectedBook(searchField, booksTable);
    }

    @FXML
    public void deleteBookOnAction() {
        booksController.deleteSelectedBook(searchField, booksTable);
    }

}


