package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.repository.BooksRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BooksRepositoryImpl implements BooksRepository {

    @Override
    public List<BookEntity> getAllBooks() {
        Session session = DBConnection.getSession();
        return session
                .createQuery("FROM BookEntity", BookEntity.class)
                .getResultList();
    }

    @Override
    public boolean hasIssueHistory(Long bookId) {
        try (Session session = DBConnection.getSession()) {
            final Long bookIdCount = session
                    .createQuery("SELECT COUNT(bie) FROM BookIssueEntity bie WHERE bie.bookEntity.bookId = :bookId", Long.class)
                    .setParameter("bookId", bookId)
                    .getSingleResult();
            return bookIdCount > 0;
        }
    }

    @Override
    public boolean deleteBook(Long bookId) {
        Transaction transaction = null;
        try (Session session = DBConnection.getSession()) {
            transaction = session.beginTransaction();
            BookEntity bookEntity = session.find(BookEntity.class, bookId);
            if (bookEntity == null) {
                transaction.rollback();
                return false;
            }
            session.remove(bookEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete book", e);
        }
    }
}
