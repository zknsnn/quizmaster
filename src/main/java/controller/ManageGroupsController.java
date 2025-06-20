package controller;

import database.mysql.GroupDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Group;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupsController {
    private GroupDAO groupDAO;
    private User user;
    @FXML
    ListView<Group> groupList;

    public void setup(User currentUser) {
        this.user = currentUser;
        this.groupDAO = new GroupDAO(Main.getDBAccess());
        groupList.getItems().clear();
        List<Group> groups = groupDAO.getAll();
        groupList.getItems().addAll(groups);
    }
    @FXML
    public void doMenu(ActionEvent actionEvent) {
        Main.getSceneManager().showWelcomeScene(user);
    }
    @FXML
    public void doCreateGroup() {
        Main.getSceneManager().showCreateUpdateGroupScene(null);
    }
    @FXML
    public void doUpdateGroup() {
        Group selectedGroup = groupList.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij updaten");
            alert.setHeaderText("Update is mislukt.");
            alert.setContentText("Selecteer een group om te updaten.");
            alert.showAndWait();
        }else {
            Main.getSceneManager().showCreateUpdateGroupScene(selectedGroup);
        }
    }
    @FXML
    public void doDeleteGroup() {}
}
//Als administrator wil ik de volledige CRUD-functionaliteit voor het beheer
//van groepen hebben (schermen manageGroups.fxml, createUpdateGroup.fxml). Let op: het
//toevoegen en verwijderen van studenten aan groepen hoort hier niet bij, daarvoor dient een
//apart scherm. Als ik een groep selecteer (scherm manageGroups.fxml) wil ik meteen kunnen
//zien hoeveel groepen er in de cursus zitten, waar de geselecteerde groep toe behoort.