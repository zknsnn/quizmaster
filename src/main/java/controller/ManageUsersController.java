package controller;

import database.mysql.UserDAO;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.User;
import view.Main;

import java.util.List;

public class ManageUsersController {

    private final UserDAO userDAO;
    private User loggedInUser;

    // FXML koppeling
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> prefixColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> passwordColumn; // toegevoegde kolom voor wachtwoorden

    @FXML
    private Label warningField;

    @FXML
    private Label countLabel;

    public ManageUsersController() {
        this.userDAO = new UserDAO(Main.getDBAccess());
    }

    //vullen tabel
    public void setup(User user) {
        this.loggedInUser = user;

        // Kolommen koppelen aan eigenschappen van User-objecten
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));

        prefixColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrefix()));

        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserRol() != null ? cellData.getValue().getUserRol().getDisplayName() : ""));

        // passwordColumn: toont wachtwoord zoals een PasswordField (gemaskeerd)
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword())); // haalt het wachtwoord op

        passwordColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setText(null);
                } else {
                    setText("●".repeat(password.length())); // toont gemaskeerde tekens!!!
                }
            }
        });

        // Gegevens ophalen en in de tabel zetten
        List<User> users = userDAO.getAll();
        userTable.setItems(FXCollections.observableArrayList(users));
        countLabel.setText("Aantal gebruikers: " + users.size());
        System.out.println("EtienneTest: Aantal gebruikers: " + users.size());

        // Bij selectie: toon aantal gebruikers met zelfde rol
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                int aantal = userDAO.countUsersByRole(newSel.getUserRol());
                countLabel.setText("Aantal gebruikers met deze rol: " + aantal);
                warningField.setVisible(false);
            }
        });

        // Dubbelklikgedrag om gebruiker te bewerken
        userTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                doUpdateUser();
            }
        });
    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    @FXML
    public void doCreateUser() {
        Main.getSceneManager().showCreateUpdateUserScene(null);
    }

    @FXML
    public void doUpdateUser() {
        User geselecteerdeUser = userTable.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            showWarning("Geen gebruiker geselecteerd.");
        } else {
            Main.getSceneManager().showCreateUpdateUserScene(geselecteerdeUser);
        }
    }

    @FXML
    public void doDeleteUser() {
        User geselecteerdeUser = userTable.getSelectionModel().getSelectedItem();
        if (geselecteerdeUser == null) {
            showWarning("Geen gebruiker geselecteerd.");
            return;
        }

        userDAO.delete(geselecteerdeUser);
        userTable.getItems().remove(geselecteerdeUser);
        countLabel.setText("Aantal gebruikers: " + userTable.getItems().size());
        warningField.setVisible(false);
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
