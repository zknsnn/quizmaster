package controller;

import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.User;
import view.Main;

import java.util.Comparator;
import java.util.List;

public class ManageUsersController {

    private final UserDAO userDAO;
    private User loggedInUser;

    @FXML
    private ListView<User> userList;

    @FXML
    private Label warningField;

    @FXML
    private Label countLabel;


    public ManageUsersController() {
        this.userDAO = new UserDAO(Main.getDBAccess());
    }

    // Wordt aangeroepen bij openen van het scherm
    public void setup(User user) {
        this.loggedInUser = user;
        refreshUserList();

        // Lijst van gebruiker met rol
        userList.setCellFactory(userListView -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    String userName = user.getUserName();
                    String naam = user.getFirstName() + " " + user.getPrefix() + " " + user.getLastName();
                    String rol = user.getUserRol().getDisplayName();
                    setText(userName + naam + " (" + rol + ")");
                }
            }
        });

        // Toon aantal gebruikers met dezelfde rol bij selectie
        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                int aantal = userDAO.countUsersByRole(newSel.getUserRol()); // gebruik newSel hier
                countLabel.setText("Aantal gebruikers met deze rol: " + aantal);
                warningField.setVisible(false);
            }
        });


        // Dubbelklik om gebruiker te bewerken
        userList.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                doUpdateUser();
            }
        });
    }

    private void refreshUserList() {
        userList.getItems().clear();
        List<User> users = userDAO.getAll();
        userList.getItems().addAll(users);
        users.sort(Comparator.comparing(User::getLastName));
        countLabel.setText("Aantal gebruikers: " + users.size());

    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    // Create New
    @FXML
    public void doCreateUser() {
        Main.getSceneManager().showCreateUpdateUserScene(null);
        // ververs scherm
      //  refreshUserList(); => NIET NODIG OMDAT JE NAAR EEN NIEUW(!) SCHERM GAAT
    }

    //Update
    @FXML
    public void doUpdateUser() {
        User geselecteerdeUser = userList.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            warningField.setText("Je moet eerst een gebruiker selecteren.");
            warningField.setVisible(true);
        } else {
            warningField.setVisible(false);
            Main.getSceneManager().showCreateUpdateUserScene(geselecteerdeUser);

            refreshUserList();
        }
    }

    //Delete
    @FXML
    public void doDeleteUser() {
        User geselecteerdeUser = userList.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            warningField.setText("Selecteer een gebruiker om te verwijderen.");
            warningField.setVisible(true);
        } else {
            userDAO.deleteUser(geselecteerdeUser.getUserName());
            refreshUserList();
            warningField.setVisible(false);
            countLabel.setText(""); // reset aantal bij verwijderen
            warningField.setText("Gebruiker is verwijderd.");

        }
    }

}
