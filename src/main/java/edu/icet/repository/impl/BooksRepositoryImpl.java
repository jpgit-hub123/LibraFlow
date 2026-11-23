package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.repository.BooksRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BooksRepositoryImpl implements BooksRepository {

    private final Session session = DBConnection.getSession();

    @Override
    public List<BookEntity> getAllBooks() {

        return session
                .createQuery("FROM BookEntity", BookEntity.class)
                .getResultList();
    }

    @Override
    public boolean hasIssueHistory(Long bookId) {
        return !session
                .createQuery("FROM BookIssueEntity bi WHERE bi.BookEntity.bookId = :bookId", BookIssueEntity.class)
                .setParameter("bookId", bookId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    @Override
    public boolean deleteBook(String isbn) {
        try {
            Transaction transaction = session.beginTransaction();
            BookEntity bookEntity = session.createQuery(
                            "FROM BookEntity b WHERE b.isbn = :isbn", BookEntity.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
            if (bookEntity == null) {
                return false;
            }
            session.remove(bookEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
