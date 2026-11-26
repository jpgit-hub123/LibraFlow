package edu.icet.model.entity;

import edu.icet.model.dto.BookDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "book_issues")
public class BookIssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private int issueId;

    @Column(name = "member_id")
    private int memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @Transient
    private String bookTitle;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    private String status;

    public BookIssueEntity() {
    }

    public BookIssueEntity(int memberId, BookEntity bookEntity, LocalDate issueDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.bookEntity = bookEntity;
        this.isbn = bookEntity.getIsbn();
        this.bookTitle = bookEntity.getTitle();
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
        this.isbn = bookEntity != null ? bookEntity.getIsbn() : null;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
        if (bookEntity != null) {
            return bookEntity.getTitle();
        }
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
