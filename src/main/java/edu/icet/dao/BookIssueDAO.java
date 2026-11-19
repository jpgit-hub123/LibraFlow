package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.BookIssue;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.icet.model.Book;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookIssueDAO {

    Session session = DBConnection.getSession();

    public List<BookIssue> getAllIssues() {

        List<Object[]> issueBookResults = session
                .createQuery("SELECT bi, b.title FROM BookIssue bi JOIN Book b ON bi.isbn = b.isbn ORDER BY bi.issueId DESC", Object[].class)
                .getResultList();

        List<BookIssue> issuedBooks = new ArrayList<>();

        for (Object[] row : issueBookResults) {
            BookIssue issuedBook = (BookIssue) row[0];
            String issuedBookTitle = (String) row[1];
            issuedBook.setBookTitle(issuedBookTitle);
            issuedBooks.add(issuedBook);
        }

        return issuedBooks;
    }

    public boolean issueBook(BookIssue bookIssue) {

        Transaction transaction = session.beginTransaction();
        Book book = session.createQuery(
                "FROM Book b WHERE b.isbn = :isbn", Book.class)
                .setParameter("isbn", bookIssue.getIsbn())
                .uniqueResult();

        if (book == null) {
            transaction.rollback();
            return false;
        }

        if (book.getQuantity() == 0) {
            transaction.rollback();
            return false;
        }

        book.setQuantity(book.getQuantity() - 1);
        bookIssue.setBook(book);
        bookIssue.setStatus("BORROWED");
        session.persist(bookIssue);
        transaction.commit();
        return true;
    }


    public int getTotalIssuedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssue bi", Long.class)
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public int getCurrentlyBorrowedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssue bi WHERE status = :status", Long.class)
                .setParameter("status", "BORROWED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public int getOverdueBooks() {
        Long overdueBooksResult = session.createQuery(
                "SELECT COUNT(bi) FROM BookIssue bi WHERE bi.returnDate IS NULL AND bi.dueDate < CURRENT_DATE",
                Long.class
        ).getSingleResult();

        return overdueBooksResult.intValue();
    }

    public int getReturnedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssue bi WHERE status = :status", Long.class)
                .setParameter("status", "RETURNED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public boolean returnBook(int issueId) {

        Transaction transaction = session.beginTransaction();
        BookIssue bookIssue = session.find(BookIssue.class, issueId, LockModeType.PESSIMISTIC_WRITE);

        if (bookIssue == null) {
            transaction.rollback();
            return false;
        }

        if (bookIssue.getReturnDate() != null) {
            transaction.rollback();
            return false;
        }

        Book book = session.find(Book.class, bookIssue.getIsbn(), LockModeType.PESSIMISTIC_WRITE);

        if (book == null) {
            transaction.rollback();
            return false;
        }

        book.setQuantity(book.getQuantity() + 1);
        bookIssue.setReturnDate(LocalDate.now());
        bookIssue.setStatus("RETURNED");
        transaction.commit();
        return true;

    }
}