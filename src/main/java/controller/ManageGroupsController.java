package controller;

import database.mysql.GroupDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Course;
import model.Group;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageGroupsController {
    private GroupDAO groupDAO;
    private User user;
    @FXML
    ListView<Group> groupList;
    @FXML
    private Label courseGroupsCountLabel;
    List<Group> groups;

    public void setup(User currentUser) {
        this.user = currentUser;
        this.groupDAO = new GroupDAO(Main.getDBAccess());
        groupList.getItems().clear();
        groups = groupDAO.getAll();
        groupList.getItems().addAll(groups);
    }

    @FXML
    public void doMenu(ActionEvent actionEvent) {
        Main.getSceneManager().showWelcomeScene(user);
    }

    @FXML
    public void doCreateGroup() {
        Main.getSceneManager().showCreateUpdateGroupScene(null, user);
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
        } else {
            Main.getSceneManager().showCreateUpdateGroupScene(selectedGroup, user);
        }
    }

    @FXML
    public void doDeleteGroup() {
        Group selectedGroupForDeleten = groupList.getSelectionModel().getSelectedItem();
        if (selectedGroupForDeleten == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij verwijderen.");
            alert.setHeaderText("Verwijderen is mislukt.");
            alert.setContentText("Selecteer een group om te verwijderen.");
            alert.showAndWait();
        } else {
            // Vraag om bevestiging voor verwijdering
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Bevestiging");
            confirmAlert.setHeaderText("Weet je zeker dat je deze groep wilt verwijderen?");
            confirmAlert.setContentText("Groep: " + selectedGroupForDeleten.getGroupName());

            Optional<ButtonType> result = confirmAlert.showAndWait();
            //Als u op OK drukt, verwijdert u de groep
            if (result.isPresent() && result.get() == ButtonType.OK) {
                groupDAO.deleteGroup(selectedGroupForDeleten.getGroupName(), selectedGroupForDeleten.getCourse().getCourseName());
                groups = groupDAO.getAll();
                groupList.getItems().setAll(groups);
                groupList.getSelectionModel().clearSelection();

                // Bericht met succesmelding
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Succes");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Groep succesvol verwijderd.");
                infoAlert.showAndWait();
            }
        }

        //Als ik een groep selecteer (scherm manageGroups.fxml) wil ik meteen kunnen
        // zien hoeveel groepen er in de cursus zitten, waar de geselecteerde groep toe behoort.

    }
    @FXML
    public void handleGroepSelectie() {
        Group selectedGroup = groupList.getSelectionModel().getSelectedItem();
        String selectedCourseName = selectedGroup.getCourse().getCourseName();
        if (selectedGroup != null) {
            //eerste manier
            /* int count = 0;
            for (Group g:groups){
                if (selectedCourseName.equals(g.getCourse().getCourseName())){
                    count++;
                }
            }
            courseGroupsCountLabel.setText("Aantal groepen in " + selectedCourseName + " cursus: " + count);*/

            //tweede manier
            long groupCount = groups.stream()
                    .filter(group -> selectedCourseName.equals(group.getCourse().getCourseName())).count();
            courseGroupsCountLabel.setText("Aantal groepen in " + selectedCourseName + " cursus: " + groupCount);
        }

    }
}