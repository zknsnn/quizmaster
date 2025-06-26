package controller;

import database.mysql.DBAccess;
import database.mysql.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import view.Main;

public class LoginController {

    @FXML
    private TextField nameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label warningField;


    DBAccess dbAccess = Main.getDBAccess();
    UserDAO userDAO = new UserDAO(dbAccess);

    public void doLogin() {
        String username = nameTextField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showWarning("Gebruikersnaam en wachtwoord zijn verplicht!");
            return;
        }
        User user = userDAO.getOneByName(username);
        //Set ingelogde user als current user in Main
        if (user != null && user.getPassword().equals(password)) {
            Main.setCurrentUser(user);
            System.out.println("Je bent ingelogd als " + Main.currentUser());
            Main.getSceneManager().showWelcomeScene(Main.currentUser());
        } else {
           showWarning("De combinatie gebruikersnaam en wachtwoord is onjuist.");  // Toon de foutmelding

        }
    }

        private void showWarning(String message) {
            warningField.setText(message);
            warningField.setVisible(true);
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            pause.setOnFinished(event -> warningField.setVisible(false));
            pause.play();
        }

    @FXML
    public void initialize() {
        passwordField.setOnAction(event -> doLogin());
    }

    public void doQuit(ActionEvent event) {
        Platform.exit();
    }
}
