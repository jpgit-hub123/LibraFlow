package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.UserEntity;
import edu.icet.repository.LoginRepository;
import javafx.scene.control.TextField;
import org.hibernate.Session;

public class LoginRepositoryImpl implements LoginRepository {

    public boolean checkLoginDetails(TextField usernameField, TextField passwordField) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            Session session = DBConnection.getSession();
            UserEntity userEntity = session
                    .createQuery("FROM UserEntity WHERE username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (userEntity != null && userEntity.getPassword_hash().equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
