package edu.icet.service;

import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.entity.BookIssueEntity;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public interface DashboardService {

    void loadPage(String fxmlFile, BorderPane rootPane);

    int getTotalBooksCount();

    int getTotalQuantityCount();

    int getTotalCategories();

    int getTotalMemberCount();

    void loadRecentTransactions(TableView<BookIssueDTO> recentTransactionsTable);
}
