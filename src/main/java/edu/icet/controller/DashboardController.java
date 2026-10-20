package edu.icet.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    public void loadPage(String fxmlFile, BorderPane rootPane) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        try {
            Parent rootNode = loader.load();
            rootPane.getScene().setRoot(rootNode);
        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }
}
