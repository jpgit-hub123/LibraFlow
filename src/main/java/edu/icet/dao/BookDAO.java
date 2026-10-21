package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDAO {

    public boolean saveBook(Book book) {

        String sql = "INSERT INTO books (isbn, title, author, category, quantity) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setInt(5, book.getQuantity());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}

