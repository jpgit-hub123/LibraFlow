package edu.icet.repository;

import edu.icet.model.entity.BookIssueEntity;
import javafx.collections.ObservableList;

public interface DashboardRepository {

    int getTotalBooks();

    int getTotalQuantity();

    int getTotalCategories();

    int getTotalMembers();

    ObservableList<BookIssueEntity> getAllIssues();
}
