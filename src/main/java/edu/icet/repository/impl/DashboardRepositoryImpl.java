package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.repository.DashboardRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepositoryImpl implements DashboardRepository {

    @Override
    public int getTotalBooks() {
        Session session = DBConnection.getSession();
        Long totalBooksResult = session
                .createQuery("SELECT COUNT(be) FROM BookEntity be", Long.class)
                .getSingleResult();
        return totalBooksResult.intValue();
    }

    @Override
    public int getTotalQuantity() {
        Session session = DBConnection.getSession();
        Long totalBookQuantityResult = session
                .createQuery("SELECT SUM(be.quantity) FROM BookEntity be", Long.class)
                .getSingleResult();
        return totalBookQuantityResult.intValue();
    }

    @Override
    public int getTotalCategories() {
        Session session = DBConnection.getSession();
        Long totalCategoryResult = session
                .createQuery("SELECT COUNT(DISTINCT be.category) FROM BookEntity be", Long.class)
                .getSingleResult();
        return totalCategoryResult.intValue();
    }

    @Override
    public int getTotalMembers() {
        Session session = DBConnection.getSession();
        Long totalMembersResult = session
                .createQuery("SELECT COUNT(me) FROM MemberEntity me", Long.class)
                .getSingleResult();
        return totalMembersResult.intValue();
    }

    @Override
    public ObservableList<BookIssueEntity> getAllIssues() {
        Session session = DBConnection.getSession();
        List<Object[]> issueBookResults = session
                .createQuery("SELECT bi, b.title FROM BookIssueEntity bi JOIN BookEntity b ON bi.isbn = b.isbn ORDER BY bi.issueId DESC", Object[].class)
                .getResultList();

        List<BookIssueEntity> issuedBooks = new ArrayList<>();

        for (Object[] row : issueBookResults) {
            BookIssueEntity issuedBook = (BookIssueEntity) row[0];
            String issuedBookTitle = (String) row[1];
            issuedBook.setBookTitle(issuedBookTitle);
            issuedBooks.add(issuedBook);
        }

        return FXCollections.observableArrayList(
                issuedBooks.stream().limit(10).toList()
        );
    }
}
