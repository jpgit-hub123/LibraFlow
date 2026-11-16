package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.User;
import javafx.scene.control.TextField;
import org.hibernate.Session;

public class UserDAO {

    public boolean checkLoginDetails(TextField usernameField, TextField passwordField) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            Session session = DBConnection.getSession();
            User user = session
                    .createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user != null && user.getPassword_hash().equals(password)) {
                System.out.println("Login successful");
                return true;
            } else {
                System.out.println("Login failed");
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
