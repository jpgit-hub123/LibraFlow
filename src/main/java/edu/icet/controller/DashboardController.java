package edu.icet.controller;

import edu.icet.dao.BookDAO;
import edu.icet.dao.MemberDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    BookDAO bookDAO;
    MemberDAO memberDAO;

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
        bookDAO = new BookDAO();
        return bookDAO.getTotalBooks();
    }

    public int getTotalQuantityCount() {
        bookDAO = new BookDAO();
        return bookDAO.getTotalQuantity();
    }

    public int getTotalCategories() {
        bookDAO = new BookDAO();
        return bookDAO.getTotalCategories();
    }

    public int getTotalMemberCount() {
        memberDAO = new MemberDAO();
        return memberDAO.getTotalMembers();
    }
}
