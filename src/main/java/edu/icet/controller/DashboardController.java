package edu.icet.controller;

import edu.icet.model.dto.BookDTO;
import edu.icet.model.dto.BookIssueDTO;
import edu.icet.model.dto.MemberDTO;
import edu.icet.model.entity.BookIssueEntity;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    BookDTO bookDTO = new BookDTO();

    public void loadPage(String fxmlFile, BorderPane rootPane) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        try {
            Parent rootNode = loader.load();
            rootPane.getScene().setRoot(rootNode);
        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

    public int getTotalBooksCount() {
        return bookDTO.getTotalBooks();
    }

    public int getTotalQuantityCount() {
        return bookDTO.getTotalQuantity();
    }

    public int getTotalCategories() {
        return bookDTO.getTotalCategories();
    }

    public int getTotalMemberCount() {
        return new MemberDTO().getTotalMembers();
    }

    public void loadRecentTransactions(TableView<BookIssueEntity> recentTransactionsTable) {
        recentTransactionsTable.setItems(
                FXCollections.observableArrayList(
                        new BookIssueDTO().getAllIssues()
                                .stream()
                                .limit(10)
                                .toList()
                )
        );
    }
}
