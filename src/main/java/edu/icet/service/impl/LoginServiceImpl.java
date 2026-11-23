package edu.icet.service.impl;

import edu.icet.repository.LoginRepository;
import edu.icet.repository.impl.LoginRepositoryImpl;
import edu.icet.service.LoginService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class LoginServiceImpl implements LoginService {

    public final LoginRepository loginRepository = new LoginRepositoryImpl();

    @Override
    public void loadPage(String fxmlFile, BorderPane rootPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Parent rootNode = loader.load();
            rootPane.getScene().setRoot(rootNode);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean authenticate(TextField usernameField, TextField passwordField) {
        return loginRepository.checkLoginDetails(usernameField, passwordField);
    }
}
