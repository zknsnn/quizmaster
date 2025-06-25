package controller;

import database.mysql.UserDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.User;
import model.UserRole;
import view.Main;

import java.util.Optional;

public class CreateUpdateUserController {

    private final UserDAO userDAO;
    private User user;

    // Constructor: maakt verbinding met de database
    public CreateUpdateUserController() {
        this.userDAO = new UserDAO(Main.getDBAccess());
    }

    // === FXML-velden gekoppeld aan je FXML-bestand ===
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

    /**
     * Deze methode wordt aangeroepen vanuit de SceneManager.
     * Als user != null is, vullen we het formulier met bestaande data (UPDATE-modus)
     */
    public void setup(User user) {
        this.user = user;
        userRoleChoiceBox.getItems().addAll(UserRole.values());

        if (user != null) {
            userNameField.setText(user.getUserName());
            userNameField.setDisable(true); // voorkomen dat de PK gewijzigd wordt
            passwordField.setText(user.getPassword());
            firstNameField.setText(user.getFirstName());
            prefixField.setText(user.getPrefix());
            lastNameField.setText(user.getLastName());
            userRoleChoiceBox.setValue(user.getUserRol());
        }
    }

//        Opslaan van nieuwe of gewijzigde gebruiker.

//        Valideer, CREATE, UPDATE feedback.
    @FXML
    public void doCreateUpdateUser() {
        //Validatie
        if (userNameField.getText().isBlank()) {
            showWarning("Gebruikersnaam verplicht.");
            return;
        }
        if (passwordField.getText().isBlank()) {
            showWarning("Wachtwoord verplicht.");
            return;
        }
        if (userRoleChoiceBox.getValue() == null) {
            showWarning("Rol verplicht, selecteer een rol.");
            return;
        }
        if (firstNameField.getText().isBlank()) {
            showWarning("Voornaam verplicht.");
            return;
        }
        if (lastNameField.getText().isBlank()) {
            showWarning("Achternaam verplicht.");
            return;
        }

        // Gegevens ophalen
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String prefix = prefixField.getText();
        String lastName = lastNameField.getText();
        UserRole userRole = userRoleChoiceBox.getValue();

        // CREATE of UPDATE uitvoeren
        if (user == null) {
            // Nieuwe gebruiker aanmaken
            User newUser = new User(userName, password, firstName, prefix, lastName, userRole);
            userDAO.storeOne(newUser);
            showWarning("Nieuwe gebruiker aangemaakt.");
        } else {
            // Bestaande gebruiker bijwerken
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setPrefix(prefix);
            user.setLastName(lastName);
            user.setUserRol(userRole);
            userDAO.updateUser(user);
            showWarning("Gebruiker bijgewerkt.");
        }

        // === Formulier tijdelijk tonen, dan resetten ===
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> clearForm());
        pause.play();
    }

    //Verwijder gebruiker na bevestiging.
    @FXML
    public void doRemove() {
        if (user == null) {
            showWarning("Selecteer een gebruiker om te verwijderen.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Verwijderen");
        alert.setHeaderText("Verwijderen van gebruiker " + user.getUserName() + "?");
        alert.setContentText("Weet je zeker dat je de gebruiker " + user.getUserName() + " wilt verwijderen?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            userDAO.delete(user); // Verwijderen op basis van User-object
            System.out.println("Gebruiker verwijderd: " + user.getUserName());
            Main.getSceneManager().showManageUsersScene(Main.currentUser());
        }
    }

    // Gaat terug naar het hoofdmenu
    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(Main.currentUser());
    }

    // Gaat terug naar het overzichtsscherm
    @FXML
    public void doCancel() {
        Main.getSceneManager().showManageUsersScene(Main.currentUser());
    }

    @FXML
    public void doBack() {
        Main.getSceneManager().showManageUsersScene(Main.currentUser());
    }

    // Reset het formulier
    private void clearForm() {
        userNameField.clear();
        passwordField.clear();
        firstNameField.clear();
        prefixField.clear();
        lastNameField.clear();
        userRoleChoiceBox.setValue(null);
        userNameField.setDisable(false); // Zet weer editable bij create
    }

    // Toont foutmelding tijdelijk
    private void showWarning(String message) {
        warningField.setText(message);
        warningField.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> warningField.setVisible(false));
        pause.play();
    }
}

//        public void doUpdate() {}
//        public void doLogin() {}

