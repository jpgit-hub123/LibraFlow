package edu.icet.model.dto;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class BookDTO {

    Session session = DBConnection.getSession();

    public boolean saveBook(BookEntity bookEntity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.persist(bookEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookEntity> getAllBooks() {
        return session
                .createQuery("FROM BookEntity", BookEntity.class)
                .getResultList();
    }

    public boolean updateBook(String originalIsbn, BookEntity bookEntity) {
        Transaction transaction = session.beginTransaction();
        BookEntity existingBookEntity = session.createQuery(
                "FROM BookEntity b WHERE b.isbn = :isbn", BookEntity.class)
                .setParameter("isbn", originalIsbn)
                .uniqueResult();
        if (existingBookEntity == null) {
            return false;
        }
        existingBookEntity.setIsbn(bookEntity.getIsbn());
        existingBookEntity.setTitle(bookEntity.getTitle());
        existingBookEntity.setAuthor(bookEntity.getAuthor());
        existingBookEntity.setCategory(bookEntity.getCategory());
        existingBookEntity.setQuantity(bookEntity.getQuantity());
        transaction.commit();
        return true;
    }

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

    public boolean hasIssueHistory(Long bookId) {
        return !session
                .createQuery("FROM BookIssueEntity bi WHERE bi.BookEntity.bookId = :bookId", BookIssueEntity.class)
                .setParameter("bookId", bookId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }



}

