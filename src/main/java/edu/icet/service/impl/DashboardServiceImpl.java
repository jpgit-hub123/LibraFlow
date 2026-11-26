package edu.icet.service.impl;

import edu.icet.mapper.IssueReturnMapper;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.repository.DashboardRepository;
import edu.icet.repository.impl.DashboardRepositoryImpl;
import edu.icet.service.DashboardService;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardServiceImpl implements DashboardService {

    public final DashboardRepository dashboardRepository = new DashboardRepositoryImpl();

    @Override
    public void loadPage(String fxmlFile, BorderPane rootPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Parent rootNode = loader.load();
            rootPane.getScene().setRoot(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getTotalBooksCount() {
        return dashboardRepository.getTotalBooks();
    }

    @Override
    public int getTotalQuantityCount() {
        return dashboardRepository.getTotalQuantity();
    }

    @Override
    public int getTotalCategories() {
        return dashboardRepository.getTotalCategories();
    }

    @Override
    public int getTotalMemberCount() {
        return dashboardRepository.getTotalMembers();
    }

    @Override
    public void loadRecentTransactions(TableView<BookIssueDTO> recentTransactionsTable) {
        IssueReturnMapper issueReturnMapper = new IssueReturnMapper();
        recentTransactionsTable.setItems(FXCollections.observableArrayList(
                dashboardRepository.getAllIssues().stream().map(issueReturnMapper::toDTO).toList()
        ));
    }
}
