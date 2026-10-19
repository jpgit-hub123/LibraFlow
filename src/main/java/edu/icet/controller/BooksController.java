package edu.icet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BooksController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> booksTable;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private void showDashboard() {
        loadPage("/edu/icet/view/Dashboard.fxml");
    }

    @FXML
    private void showAddBookForm() {
        System.out.println("Add Book button clicked!");
    }

    private void loadPage(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(path)
            );

            Parent page = loader.load();

            BorderPane currentRoot =
                    (BorderPane) searchField.getScene().getRoot();

            currentRoot.getScene().setRoot(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
