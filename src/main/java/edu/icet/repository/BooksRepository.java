package edu.icet.repository;

import edu.icet.model.entity.BookEntity;

import java.util.List;

public interface BooksRepository {

    List<BookEntity> getAllBooks();

    boolean hasIssueHistory(Long bookId);

    boolean deleteBook(String isbn);
}
