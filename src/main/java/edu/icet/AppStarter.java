package edu.icet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppStarter extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Label welcomeLabel = new Label("Welcome to LibraFlow Library Management System!");

        StackPane root = new StackPane(welcomeLabel);

        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("LibraFlow");
        stage.setScene(scene);
        stage.show();

    }
}

