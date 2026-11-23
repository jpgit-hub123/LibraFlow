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

    private final Session session = DBConnection.getSession();

    @Override
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

    @Override
    public List<BookEntity> getAllBooks() {
        return session
                .createQuery("FROM BookEntity", BookEntity.class)
                .getResultList();
    }

    @Override
    public List<MemberEntity> getAllMembers() {
        return session
                .createQuery("FROM MemberEntity", MemberEntity.class)
                .getResultList();
    }

    @Override
    public List<BookIssueEntity> getAllIssues() {
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
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie", Long.class)
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
    public int getCurrentlyBorrowedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie WHERE status = :status", Long.class)
                .setParameter("status", "BORROWED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
    public int getOverdueBooks() {
        Long overdueBooksResult = session.createQuery(
                "SELECT COUNT(bie) FROM BookIssueEntity bie WHERE bie.returnDate IS NULL AND bie.dueDate < CURRENT_DATE",
                Long.class
        ).getSingleResult();
        return overdueBooksResult.intValue();
    }

    @Override
    public int getReturnedBooks() {
        Long totalIssuedBooksResult = session
                .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie WHERE status = :status", Long.class)
                .setParameter("status", "RETURNED")
                .getSingleResult();
        return totalIssuedBooksResult.intValue();
    }

    @Override
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
