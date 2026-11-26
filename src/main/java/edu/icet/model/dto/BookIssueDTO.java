package edu.icet.model.dto;

import java.time.LocalDate;

public class BookIssueDTO {

    private int issueId;
    private int memberId;
    private Long bookId;
    private String isbn;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public BookIssueDTO() {
    }

    public BookIssueDTO(int issueId, int memberId, Long bookId, String isbn, String bookTitle, LocalDate issueDate,
                        LocalDate dueDate, LocalDate returnDate, String status) {
        this.issueId = issueId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
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

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
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
