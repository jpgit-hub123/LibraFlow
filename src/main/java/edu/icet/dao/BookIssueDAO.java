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

        List<BookIssue> issues = new ArrayList<>();

        String sql = "SELECT issue_id, member_id, isbn, issue_date, due_date, return_date, status FROM book_issues " +
                "ORDER BY issue_id DESC";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Date returnDate = resultSet.getDate("return_date");

                BookIssue issue = new BookIssue(
                        resultSet.getInt("issue_id"),
                        resultSet.getInt("member_id"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("issue_date").toLocalDate(),
                        resultSet.getDate("due_date").toLocalDate(),
                        returnDate == null ? null : returnDate.toLocalDate(),
                        resultSet.getString("status")
                );
                issues.add(issue);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return issues;
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
                try (PreparedStatement checkStatement =
                             connection.prepareStatement(checkBookSql)) {

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
}