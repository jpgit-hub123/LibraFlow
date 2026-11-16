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

    Session session = DBConnection.getSession();

    public boolean saveBook(Book book) {
        try {
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
        return !session
                .createQuery("FROM bookIssue bi WHERE bi.isbn = :isbn", BookIssue.class)
                .setParameter("isbn", isbn)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    public int getTotalBooks() {
        Long totalBooksResult = session
                .createQuery("SELECT COUNT(b) FROM Book b", Long.class)
                .getSingleResult();
        return totalBooksResult.intValue();
    }

    public int getTotalQuantity() {
        Long totalBookQuantityResult = session
                .createQuery("SELECT SUM(b.quantity) FROM Book b", Long.class)
                .getSingleResult();
        return totalBookQuantityResult.intValue();
    }

    public int getTotalCategories() {
        Long totalCategoryResult = session
                .createQuery("SELECT COUNT(DISTINCT b.category) FROM Book b", Long.class)
                .getSingleResult();
        return totalCategoryResult.intValue();
    }

}

