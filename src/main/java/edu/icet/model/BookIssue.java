package edu.icet.model;

import java.time.LocalDate;

public class BookIssue {

    private int issueId;
    private int memberId;
    private String isbn;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public BookIssue(int issueId, int memberId, String isbn, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate,
                     String status) {
        this.issueId = issueId;
        this.memberId = memberId;
        this.isbn = isbn;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.returnDate = returnDate;
    }

    public BookIssue(int memberId, String isbn, LocalDate issueDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.isbn = isbn;
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
