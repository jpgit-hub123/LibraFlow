package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookEntity;
import edu.icet.repository.AddBookRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddBookRepositoryImpl implements AddBookRepository {

    private final Session session = DBConnection.getSession();

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

    public boolean updateBook(String originalIsbnOfEditedBook, BookEntity updatedBookEntity) {
        Transaction transaction = session.beginTransaction();
        BookEntity existingBookEntity = session.createQuery(
                        "FROM BookEntity b WHERE b.isbn = :isbn", BookEntity.class)
                .setParameter("isbn", originalIsbnOfEditedBook)
                .uniqueResult();
        if (existingBookEntity == null) {
            return false;
        }
        existingBookEntity.setIsbn(updatedBookEntity.getIsbn());
        existingBookEntity.setTitle(updatedBookEntity.getTitle());
        existingBookEntity.setAuthor(updatedBookEntity.getAuthor());
        existingBookEntity.setCategory(updatedBookEntity.getCategory());
        existingBookEntity.setQuantity(updatedBookEntity.getQuantity());
        transaction.commit();
        return true;
    }
}
