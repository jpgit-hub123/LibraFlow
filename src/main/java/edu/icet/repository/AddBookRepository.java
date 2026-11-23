package edu.icet.repository;

import edu.icet.model.entity.BookEntity;

public interface AddBookRepository {

    boolean saveBook(BookEntity bookEntity);

    boolean updateBook(String originalIsbnOfEditedBook, BookEntity updatedBookEntity);
}

