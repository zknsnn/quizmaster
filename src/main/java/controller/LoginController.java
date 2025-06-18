package controller;

import database.mysql.CourseDAO;
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

    DBAccess dbAccess = Main.getDBAccess();
    UserDAO userDAO = new UserDAO(dbAccess);
    private CourseDAO courseDAO = new CourseDAO(dbAccess);


    public void doLogin() {
        String username = nameTextField.getText();
        String password = passwordField.getText();

        User user = userDAO.getOneByName(username);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Je bent ingelogd als " + username);
            Main.getSceneManager().showWelcomeScene(user);
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
