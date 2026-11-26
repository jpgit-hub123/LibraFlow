package edu.icet.mapper;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;

public class IssueReturnMapper {

    public BookIssueDTO toDTO(BookIssueEntity bookIssueEntity) {
        if (bookIssueEntity == null) {
            return null;
        }
        return new BookIssueDTO(
                bookIssueEntity.getIssueId(),
                bookIssueEntity.getMemberId(),
                bookIssueEntity.getBookEntity().getBookId(),
                bookIssueEntity.getIsbn(),
                bookIssueEntity.getBookTitle(),
                bookIssueEntity.getIssueDate(),
                bookIssueEntity.getDueDate(),
                bookIssueEntity.getReturnDate(),
                bookIssueEntity.getStatus()
        );
    }

    public BookIssueEntity toEntity(BookIssueDTO bookIssueDTO, BookEntity bookEntity) {
        if (bookIssueDTO == null) {
            return null;
        }
        BookIssueEntity bookIssueEntity = new BookIssueEntity(
                bookIssueDTO.getMemberId(),
                bookEntity,
                bookIssueDTO.getIssueDate(),
                bookIssueDTO.getDueDate()
        );
        bookIssueEntity.setReturnDate(bookIssueDTO.getReturnDate());
        bookIssueEntity.setStatus(bookIssueDTO.getStatus());
        return bookIssueEntity;
    }
}
