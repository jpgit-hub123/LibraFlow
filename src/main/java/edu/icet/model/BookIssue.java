package edu.icet.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "book_issues")
public class BookIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private int issueId;

    @Column(name = "member_id")
    private int memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

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

    public BookIssue() {
    }

    public BookIssue(int memberId, Book book, LocalDate issueDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.book = book;
        this.isbn = book.getIsbn();
        this.bookTitle = book.getTitle();
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

//    public BookIssue(int memberId, String isbn, LocalDate issueDate, LocalDate dueDate) {
//        this.memberId = memberId;
//        this.isbn = isbn;
//        this.issueDate = issueDate;
//        this.dueDate = dueDate;
//    }

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        this.isbn = book != null ? book.getIsbn() : null;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
        if (book != null) {
            return book.getTitle();
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

    public boolean isOverdue() {
        return "BORROWED".equals(status) && dueDate != null && dueDate.isBefore(java.time.LocalDate.now());
    }

    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, java.time.LocalDate.now());
    }

    public double getFineAmount() {
        double finePerDay = 20.0;
        return getOverdueDays() * finePerDay;
    }
}
