package controller;

import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;
import model.UserRole;
import view.Main;

public class CreateUpdateUserController {

    private final UserDAO userDAO;
    private User user; //null bij nieuwe gebruiker. deze opmerking begrijp ik niet zo

    //verbinding Database
    public CreateUpdateUserController() {
        this.userDAO = new UserDAO(Main.getDBAccess());
    }

    //velden
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField prefixField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ChoiceBox<UserRole> userRoleChoiceBox;
    @FXML
    private Label warningField;


    //Scene manager vraagt om info
    public void setup(User user) {
        this.user = user;
        userRoleChoiceBox.getItems().addAll(UserRole.values());


        // user != null ==> UPDATE
        if (user != null) {
            userNameField.setText(user.getUserName());
            passwordField.setText(user.getPassword());
            firstNameField.setText(user.getFirstName());
            prefixField.setText(user.getPrefix());
            lastNameField.setText(user.getLastName());
            userRoleChoiceBox.setValue(user.getUserRol());
        }
    }


    @FXML
    public void doCreateUpdateUser() {
        if (userNameField.getText().isBlank() || passwordField.getText().isBlank()) {
            warningField.setText("Gebruikersnaam en wachtwoord verplicht.");
            warningField.setVisible(true);
            return;
        }
        if (userRoleChoiceBox.getValue() == null) {
            warningField.setText("Rol verplicht, selecteer een rol.");
            warningField.setVisible(true);
            return;
        }

        //data ophalen uit 6 invoervelden
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String prefix = prefixField.getText();
        String lastName = lastNameField.getText();
        UserRole userRole = userRoleChoiceBox.getValue();

        // user == null dus CREATE
        if (user == null){
            // create new user, daarom contact maken met de db, middels DAO
            User newUser = new User(userName, password, firstName, prefix, lastName, userRole);
            userDAO.storeOne(newUser);
            System.out.println("EtienneTest, Create new User: " + newUser.getUserName() + " is gemaakt.");
        } else {
                // DAO UPDATE
        //        user.setUserName(userName);
                user.setPassword(password);
                user.setFirstName(firstName);
                user.setPrefix(prefix);
                user.setLastName(lastName);
                user.setUserRol(userRole);
                userDAO.updateUser(user);
        }
            // terug naar overzicht
            Main.getSceneManager().showManageUsersScene(Main.currentUser());
    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(Main.currentUser());
    }

    @FXML
        public void doCancel() {
            Main.getSceneManager().showManageUsersScene(Main.currentUser());
    }
    @FXML
        public void doBack() {
            Main.getSceneManager().showManageUsersScene(Main.currentUser());
    }
}


//        public void doRemove() {}
//        public void doUpdate() {}
//        public void doLogin() {}

