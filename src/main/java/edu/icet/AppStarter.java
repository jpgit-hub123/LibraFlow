package edu.icet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class AppStarter extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/icet/view/LoginPage.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(
                        "/edu/icet/css/dashboard.css")).toExternalForm()
        );

        stage.setTitle("LibraFlow | Login");
        stage.setResizable(false);
//        stage.setMinWidth(1000);
//        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }
}

