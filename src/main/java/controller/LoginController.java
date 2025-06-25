package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import view.Main;

public class LoginController {

    @FXML
    private TextField nameTextField;
    @FXML
    private PasswordField passwordField;

    DBAccess dbAccess = Main.getDBAccess();
    UserDAO userDAO = new UserDAO(dbAccess);

    public void doLogin() {
        String username = nameTextField.getText();
        String password = passwordField.getText();

        User user = userDAO.getOneByName(username);
        //Set ingelogde user als current user in Main
        if (user != null && user.getPassword().equals(password)) {
            Main.setCurrentUser(user);
            System.out.println("Je bent ingelogd als " + Main.currentUser());
            Main.getSceneManager().showWelcomeScene(Main.currentUser());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij inloggen");
            alert.setHeaderText("Inloggen mislukt");
            alert.setContentText("De combinatie van gebruikersnaam en wachtwoord is onjuist.");
            alert.showAndWait();  // Toon de foutmelding

        }
    }

    public void doQuit(ActionEvent event) {
        Platform.exit();
    }
}
