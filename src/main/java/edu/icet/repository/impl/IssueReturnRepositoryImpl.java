package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import edu.icet.repository.IssueReturnRepository;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueReturnRepositoryImpl implements IssueReturnRepository {

    @Override
    public boolean issueBook(BookIssueEntity bookIssueEntity) {
        Session session = DBConnection.getSession();
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

    @Override
    public List<BookEntity> getAllBooks() {
        Session session = DBConnection.getSession();
        return session
                .createQuery("FROM BookEntity", BookEntity.class)
                .getResultList();
    }

    @Override
    public List<MemberEntity> getAllMembers() {
        Session session = DBConnection.getSession();
        return session
                .createQuery("FROM MemberEntity", MemberEntity.class)
                .getResultList();
    }

    @Override
    public List<BookIssueEntity> getAllIssues() {
        Session session = DBConnection.getSession();
        List<Object[]> issueBookResults = session
                .createQuery("SELECT bie, be.title FROM BookIssueEntity bie JOIN BookEntity be ON bie.bookEntity.bookId = be.bookId ORDER BY bie.issueId DESC", Object[].class)
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

    @Override
    public int getTotalIssuedBooks() {
        Session session = DBConnection.getSession();
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie", Long.class)
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
    public int getCurrentlyBorrowedBooks() {
        Session session = DBConnection.getSession();
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie WHERE status = :status", Long.class)
                .setParameter("status", "BORROWED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
    public int getOverdueBooks() {
        Session session = DBConnection.getSession();
        Long overdueBooksResult = session.createQuery(
                "SELECT COUNT(bie) FROM BookIssueEntity bie WHERE bie.returnDate IS NULL AND bie.dueDate < CURRENT_DATE",
                Long.class
        ).getSingleResult();
        return overdueBooksResult.intValue();
    }

    @Override
    public int getReturnedBooks() {
        Session session = DBConnection.getSession();
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie WHERE status = :status", Long.class)
                .setParameter("status", "RETURNED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
    public boolean returnBook(int issueId) {
        Session session = DBConnection.getSession();
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
