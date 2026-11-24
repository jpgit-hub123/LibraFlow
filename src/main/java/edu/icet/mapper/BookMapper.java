package edu.icet.mapper;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.entity.BookEntity;

public class BookMapper {

    public BookDTO toDTO(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }
        return new BookDTO(
                bookEntity.getBookId(),
                bookEntity.getIsbn(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getCategory(),
                bookEntity.getQuantity()
        );
    }

    public BookEntity toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        return new BookEntity(
                bookDTO.getIsbn(),
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getCategory(),
                bookDTO.getQuantity()
        );
    }
}
