package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
        return session
                .createQuery("FROM Book", Book.class)
                .getResultList();
    }

    public boolean updateBook(String originalIsbn, Book book) {
        Transaction transaction = session.beginTransaction();
        Book existingBook = session.createQuery(
                "FROM Book b WHERE b.isbn = :isbn", Book.class)
                .setParameter("isbn", originalIsbn)
                .uniqueResult();
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
            Book book = session.createQuery(
                    "FROM Book b WHERE b.isbn = :isbn", Book.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
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

    public boolean hasIssueHistory(Long bookId) {
        return !session
                .createQuery("FROM BookIssue bi WHERE bi.book.bookId = :bookId", BookIssue.class)
                .setParameter("bookId", bookId)
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

