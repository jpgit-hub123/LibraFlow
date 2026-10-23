package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.BookIssue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BookIssueDAO {

    public List<BookIssue> getAllIssues() {

        List<BookIssue> issuedBooks = new ArrayList<>();

        String sql = "SELECT bi.issue_id, bi.member_id, bi.isbn, b.title AS book_title, bi.issue_date, bi.due_date, bi.return_date, " +
                "bi.status FROM book_issues bi JOIN books b ON bi.isbn = b.isbn ORDER BY bi.issue_id DESC";
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Date returnDate = resultSet.getDate("return_date");

                BookIssue issuedBook = new BookIssue(
                        resultSet.getInt("issue_id"),
                        resultSet.getInt("member_id"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("issue_date").toLocalDate(),
                        resultSet.getDate("due_date").toLocalDate(),
                        returnDate == null ? null : returnDate.toLocalDate(),
                        resultSet.getString("status")
                );
                issuedBook.setBookTitle(resultSet.getString("book_title"));
                issuedBooks.add(issuedBook);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return issuedBooks;
    }

    public boolean issueBook(BookIssue bookIssue) {

        String checkBookSql = "SELECT quantity FROM books WHERE isbn = ? FOR UPDATE";

        String updateBookSql = "UPDATE books SET quantity = quantity - 1 WHERE isbn = ?";

        String insertIssueSql = "INSERT INTO book_issues " + "(member_id, isbn, issue_date, due_date, status) " +
                                "VALUES (?, ?, ?, ?, 'BORROWED')";

        try {
            Connection connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            try {
                // 1. Check book availability
                int quantity;
                try (PreparedStatement checkStatement = connection.prepareStatement(checkBookSql)) {

                    checkStatement.setString(1, bookIssue.getIsbn());

                    try (ResultSet resultSet = checkStatement.executeQuery()) {

                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }
                        quantity = resultSet.getInt("quantity");
                    }
                }
                if (quantity <= 0) {
                    connection.rollback();
                    return false;
                }
                // 2. Reduce available quantity
                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateBookSql)) {

                    updateStatement.setString(1, bookIssue.getIsbn());
                    updateStatement.executeUpdate();
                }
                // 3. Save issue details
                try (PreparedStatement insertStatement =
                             connection.prepareStatement(insertIssueSql)) {

                    insertStatement.setInt(1, bookIssue.getMemberId());
                    insertStatement.setString(2, bookIssue.getIsbn());
                    insertStatement.setDate(
                            3, Date.valueOf(bookIssue.getIssueDate()));
                    insertStatement.setDate(
                            4, Date.valueOf(bookIssue.getDueDate()));

                    insertStatement.executeUpdate();
                }
                // 4. Save both changes
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean returnBook(int issueId) {

        String selectSql =
                "SELECT isbn FROM book_issues " +
                        "WHERE issue_id = ? AND status = 'BORROWED' FOR UPDATE";

        String updateIssueSql =
                "UPDATE book_issues " +
                        "SET status = 'RETURNED', return_date = ? " +
                        "WHERE issue_id = ? AND status = 'BORROWED'";

        String updateBookSql =
                "UPDATE books SET quantity = quantity + 1 WHERE isbn = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                String isbn;

                // 1. Find the borrowed book
                try (PreparedStatement statement =
                             connection.prepareStatement(selectSql)) {

                    statement.setInt(1, issueId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }

                        isbn = resultSet.getString("isbn");
                    }
                }

                // 2. Update issue status and return date
                try (PreparedStatement statement =
                             connection.prepareStatement(updateIssueSql)) {

                    statement.setDate(1, Date.valueOf(java.time.LocalDate.now()));
                    statement.setInt(2, issueId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                // 3. Increase available book quantity
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSql)) {

                    statement.setString(1, isbn);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException("Book quantity could not be updated.");
                    }
                }

                // 4. Commit both changes
                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public int getTotalIssuedBooks() {

        String sql = "SELECT COUNT(*) FROM book_issues";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getCurrentlyBorrowedBooks() {

        String sql = """
            SELECT COUNT(*)
            FROM book_issues
            WHERE status = 'BORROWED'
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getOverdueBooks() {

        String sql = """
            SELECT COUNT(*)
            FROM book_issues
            WHERE status = 'BORROWED'
              AND due_date < CURRENT_DATE
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getReturnedBooks() {

        String sql = """
            SELECT COUNT(*)
            FROM book_issues
            WHERE status = 'RETURNED'
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}