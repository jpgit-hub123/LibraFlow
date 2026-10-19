package edu.icet.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class DashboardPageController {

    @FXML
    private BorderPane rootPane;

    public DashboardController dashboardController = new DashboardController();

    @FXML
    private void showDashboardOnAction() {
        dashboardController.loadPage("/edu/icet/view/Dashboard.fxml", rootPane);
    }

    @FXML
    private void showBooksOnAction() {
        dashboardController.loadPage("/edu/icet/view/Books.fxml", rootPane);
    }

}