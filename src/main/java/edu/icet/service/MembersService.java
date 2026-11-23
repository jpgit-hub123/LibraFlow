package edu.icet.service;

import edu.icet.model.entity.MemberEntity;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public interface MembersService {

    void setMembersDataIntoTable(TextField searchField, TableView<MemberEntity> membersTable, TableColumn<MemberEntity,
            Number> memberIdColumn, TableColumn<MemberEntity, String> nameColumn, TableColumn<MemberEntity,
            String> emailColumn, TableColumn<MemberEntity, String> phoneColumn, TableColumn<MemberEntity,
            String> addressColumn);

    void getSearchMember(TextField searchField, TableView<MemberEntity> membersTable);

    void loadForm(TextField searchField, TableView<MemberEntity> membersTable);

    void editMembersInfo(TextField searchField, TableView<MemberEntity> membersTable);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void removeMember(TextField searchField, TableView<MemberEntity> membersTable);

    void loadMembers(TextField searchField, TableView<MemberEntity> membersTable);

    void loadPage(String s, TextField searchField);
}
