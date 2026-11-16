package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public boolean saveBook(Book book) {
        try {
            Session session = DBConnection.getSession();
            Transaction transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT isbn, title, author, category, quantity FROM books";

        try {

            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Book book = new Book(
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return books;
    }

    public boolean updateBook(String originalIsbn, Book book) {
        Session session = DBConnection.getSession();
        Transaction transaction = session.beginTransaction();
        Book existingBook = session.find(Book.class, originalIsbn);
        if (existingBook == null) {
            return false;
        }
        existingBook.setIsbn(book.getIsbn());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setCategory(book.getCategory());
        existingBook.setQuantity(book.getQuantity());
        transaction.commit();
        return true;
    }

    public boolean deleteBook(String isbn) {
        try {
            Session session = DBConnection.getSession();
            Transaction transaction = session.beginTransaction();
            Book book = session.find(Book.class, isbn);
            if (book == null) {
                return false;
            }
            session.remove(book);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasIssueHistory(String isbn) {
        Session session = DBConnection.getSession();
        return !session
                .createQuery("FROM bookIssues bi WHERE bi.isbn = :isbn", BookIssue.class)
                .setParameter("isbn", isbn)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    private boolean hasAnIssueHistory(String isbn) {

        String sql = "SELECT COUNT(*) FROM book_issues where isbn = ? AND status != 'RETURNED'";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, isbn);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }


    }

    public int getTotalBooks() {

        String sql = "SELECT COUNT(*) FROM books";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return 0;
    }

    public int getTotalQuantity() {

        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM books";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return 0;
    }

    public int getTotalCategories() {

        String sql = "SELECT COUNT(DISTINCT category) FROM books";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return 0;
    }
}

