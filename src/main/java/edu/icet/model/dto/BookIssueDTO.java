package edu.icet.model.dto;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookIssueDTO {

    Session session = DBConnection.getSession();

    public List<BookIssueEntity> getAllIssues() {

        List<Object[]> issueBookResults = session
                .createQuery("SELECT bi, b.title FROM BookIssueEntity bi JOIN BookEntity b ON bi.isbn = b.isbn ORDER BY bi.issueId DESC", Object[].class)
                .getResultList();

        List<BookIssueEntity> issuedBooks = new ArrayList<>();

        for (Object[] row : issueBookResults) {
            BookIssueEntity issuedBook = (BookIssueEntity) row[0];
            String issuedBookTitle = (String) row[1];
            issuedBook.setBookTitle(issuedBookTitle);
            issuedBooks.add(issuedBook);
        }

        return issuedBooks;
    }

    public boolean issueBook(BookIssueEntity bookIssueEntity) {

        Transaction transaction = session.beginTransaction();
        BookEntity bookEntity = session.createQuery(
                "FROM BookEntity b WHERE b.isbn = :isbn", BookEntity.class)
                .setParameter("isbn", bookIssueEntity.getIsbn())
                .uniqueResult();

        if (bookEntity == null) {
            transaction.rollback();
            return false;
        }

        if (bookEntity.getQuantity() == 0) {
            transaction.rollback();
            return false;
        }

        bookEntity.setQuantity(bookEntity.getQuantity() - 1);
        bookIssueEntity.setBook(bookEntity);
        bookIssueEntity.setStatus("BORROWED");
        session.persist(bookIssueEntity);
        transaction.commit();
        return true;
    }


    public int getTotalIssuedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssueEntity bi", Long.class)
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public int getCurrentlyBorrowedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssueEntity bi WHERE status = :status", Long.class)
                .setParameter("status", "BORROWED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public int getOverdueBooks() {
        Long overdueBooksResult = session.createQuery(
                "SELECT COUNT(bi) FROM BookIssueEntity bi WHERE bi.returnDate IS NULL AND bi.dueDate < CURRENT_DATE",
                Long.class
        ).getSingleResult();

        return overdueBooksResult.intValue();
    }

    public int getReturnedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bi) FROM BookIssueEntity bi WHERE status = :status", Long.class)
                .setParameter("status", "RETURNED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    public boolean returnBook(int issueId) {

        Transaction transaction = session.beginTransaction();
        BookIssueEntity bookIssueEntity = session.find(BookIssueEntity.class, issueId, LockModeType.PESSIMISTIC_WRITE);

        if (bookIssueEntity == null) {
            transaction.rollback();
            return false;
        }

        if (bookIssueEntity.getReturnDate() != null) {
            transaction.rollback();
            return false;
        }

        BookEntity bookEntity = session.find(BookEntity.class, bookIssueEntity.getIsbn(), LockModeType.PESSIMISTIC_WRITE);

        if (bookEntity == null) {
            transaction.rollback();
            return false;
        }

        bookEntity.setQuantity(bookEntity.getQuantity() + 1);
        bookIssueEntity.setReturnDate(LocalDate.now());
        bookIssueEntity.setStatus("RETURNED");
        transaction.commit();
        return true;

    }
}