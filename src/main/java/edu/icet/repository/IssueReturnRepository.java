package edu.icet.repository;

import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;

import java.util.List;

public interface IssueReturnRepository {

    boolean issueBook(BookIssueEntity bookIssueEntity);

    List<BookEntity> getAllBooks();

    List<MemberEntity> getAllMembers();

    List<BookIssueEntity> getAllIssues();

    boolean returnBook(int issueId);

    int getTotalIssuedBooks();

    int getCurrentlyBorrowedBooks();

    int getOverdueBooks();

    int getReturnedBooks();
}
