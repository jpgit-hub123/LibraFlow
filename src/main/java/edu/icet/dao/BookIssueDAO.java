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
}