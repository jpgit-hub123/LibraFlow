package edu.icet.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;

public class IssueReturnController {


    public void loadPage(String fxmlFile, TextField searchField) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        try {

            Parent rootNode = loader.load();
            searchField.getScene().setRoot(rootNode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
