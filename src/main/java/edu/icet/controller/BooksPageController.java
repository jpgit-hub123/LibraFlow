package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.service.BooksService;
import edu.icet.service.impl.BooksServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BooksPageController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BookDTO> booksTable;

    @FXML
    private TableColumn<BookDTO, String> isbnColumn;

    @FXML
    private TableColumn<BookDTO, String> titleColumn;

    @FXML
    private TableColumn<BookDTO, String> authorColumn;

    @FXML
    private TableColumn<BookDTO, String> categoryColumn;

    @FXML
    private TableColumn<BookDTO, Integer> quantityColumn;

    @FXML
    private final BooksService booksServiceImpl = new BooksServiceImpl();

    @FXML
    private void showDashboardOnAction() {
        booksServiceImpl.loadPage("/edu/icet/view/Dashboard.fxml", searchField);
    }

    @FXML
    public void showMembersOnAction() {
        booksServiceImpl.loadPage("/edu/icet/view/Members.fxml", searchField);
    }

    @FXML
    public void showIssueReturnOnAction() {
        booksServiceImpl.loadPage("/edu/icet/view/IssueReturn.fxml", searchField);
    }

    @FXML
    public void logoutOnAction() {
        booksServiceImpl.loadPage("/edu/icet/view/LoginPage.fxml", searchField);
    }

    @FXML
    private void showAddBookFormOnAction() {
        booksServiceImpl.loadForm(searchField, booksTable);
    }

    @FXML
    public void initialize() {
        booksServiceImpl.setBooksDataIntoTable(isbnColumn, titleColumn, authorColumn, categoryColumn, quantityColumn, booksTable);
    }

    @FXML
    public void searchBooks() {
        booksServiceImpl.getRelatedBooks(searchField, booksTable);
    }

    @FXML
    public void editBookOnAction() {
        booksServiceImpl.editTheSelectedBook(searchField, booksTable);
    }

    @FXML
    public void deleteBookOnAction() {
        booksServiceImpl.deleteSelectedBook(searchField, booksTable);
    }

}


