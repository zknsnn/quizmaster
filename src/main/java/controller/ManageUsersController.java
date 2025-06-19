package controller;

import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.User;
import view.Main;

import java.util.List;

public class ManageUsersController {

    private final UserDAO userDAO;
    private User loggedInUser;  // om later terug te kunnen naar juiste homescreen

    @FXML
    ListView<User> userList;

    @FXML
    TextField warningField;

    public ManageUsersController() {
        this.userDAO = new UserDAO(Main.getDBAccess());
    }

    // Wordt aangeroepen bij openen van het scherm
    public void setup(User user) {
        this.loggedInUser = user;
        List<User> users = userDAO.getAll();
        userList.getItems().setAll(users);
        warningField.setVisible(false); // verberg waarschuwingen bij opstart
    }

    // 🔙 Terug naar welkomstscherm van de ingelogde gebruiker
    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    // ➕ Maak een nieuwe gebruiker aan
    @FXML
    public void doCreateUser() {
        Main.getSceneManager().showCreateUpdateUserScene(null); // null = nieuw
    }

    // Wijzig een geselecteerde gebruiker
    @FXML
    public void doUpdateUser() {
        User geselecteerdeUser = userList.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            warningField.setVisible(true);
            warningField.setText("Je moet eerst een gebruiker selecteren.");
        } else {
            Main.getSceneManager().showCreateUpdateUserScene(geselecteerdeUser);
        }
    }

    // 🗑️ Verwijder een geselecteerde gebruiker
    @FXML
    public void doDeleteUser() {
        User geselecteerdeUser = userList.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            warningField.setVisible(true);
            warningField.setText("Selecteer een gebruiker om te verwijderen.");
        } else {
            userDAO.deleteUser(geselecteerdeUser.getUserName());
            userList.getItems().remove(geselecteerdeUser);
            warningField.setVisible(false);
        }
    }
}
