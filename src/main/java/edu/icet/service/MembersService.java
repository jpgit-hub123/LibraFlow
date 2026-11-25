package edu.icet.service;

import edu.icet.model.dto.MemberDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public interface MembersService {

    void setMembersDataIntoTable(TextField searchField, TableView<MemberDTO> membersTable,
                                 TableColumn<MemberDTO, Number> memberIdColumn, TableColumn<MemberDTO, String> nameColumn,
                                 TableColumn<MemberDTO, String> emailColumn, TableColumn<MemberDTO, String> phoneColumn,
                                 TableColumn<MemberDTO, String> addressColumn);

    void getSearchMember(TextField searchField, TableView<MemberDTO> membersTable);

    void loadForm(TextField searchField, TableView<MemberDTO> membersTable);

    void editMembersInfo(TextField searchField, TableView<MemberDTO> membersTable);

    void showAlert(Alert.AlertType alertType, String info, String message);

    void removeMember(TextField searchField, TableView<MemberDTO> membersTable);

    void loadMembers(TextField searchField, TableView<MemberDTO> membersTable);

    void loadPage(String fxmlFile, TextField searchField);
}

