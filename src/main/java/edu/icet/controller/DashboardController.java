package edu.icet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private void showDashboard() {
        loadPage("/edu/icet/view/Dashboard.fxml");
    }

    @FXML
    private void showBooks() {
        loadPage("/edu/icet/view/Books.fxml");
    }

    private void loadPage(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(path)
            );

            Parent page = loader.load();

            rootPane.getScene().setRoot(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}