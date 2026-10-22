package edu.icet.controller;

import edu.icet.dao.MemberDAO;
import edu.icet.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class MembersController {

    private ObservableList<Member> membersList;

    public void setMembersDataIntoTable(TableView<Member> membersTable,
                                        TableColumn<Member, Number> memberIdColumn,
                                        TableColumn<Member, String> nameColumn,
                                        TableColumn<Member, String> emailColumn,
                                        TableColumn<Member, String> phoneColumn,
                                        TableColumn<Member, String> addressColumn) {

        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        MemberDAO memberDAO = new MemberDAO();
        membersList = FXCollections.observableArrayList(memberDAO.getAllMembers());
        membersTable.setItems(membersList);
    }

    public void loadPage(String fxmlFile, TextField searchField) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            Parent rootNode = loader.load();

            searchField.getScene().setRoot(rootNode);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }
}