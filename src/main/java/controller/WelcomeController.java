package controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import model.Question;
import model.Quiz;
import model.User;
import model.UserRole;
import view.Main;

public class WelcomeController {
    private User user;
    private Quiz quiz;
    private Question question;

    @FXML
    private Label welcomeLabel;
    @FXML
    private MenuButton taskMenuButton;

    public void setup(User user) {
        this.user = user;
        taskMenuButton.getItems().clear();
        welcomeLabel.setText("Welkom, " + this.user.getFirstName() + " ! Maak een keuze uit het menu.");

        UserRole role = Main.currentUser().getUserRol();

        if (role == UserRole.STUDENT) {
            MenuItem item1 = new MenuItem("In- of uitschrijven cursus");
            item1.setOnAction(e -> {Main.getSceneManager().showStudentSignInOutScene(Main.currentUser());});
            MenuItem item2 = new MenuItem("Maak een quiz");
            item2.setOnAction(e -> {Main.getSceneManager().showSelectQuizForStudent(Main.currentUser());});
            taskMenuButton.getItems().addAll(item1, item2);
        } else if (role == UserRole.COÖRDINATOR) {
            MenuItem item1 = new MenuItem("Beheer quizzen");
            item1.setOnAction(e -> {Main.getSceneManager().showManageQuizScene(Main.currentUser());});
            MenuItem item2 = new MenuItem("Coordinator Dashboard");
            item2.setOnAction(e -> {Main.getSceneManager().showCoordinatorDashboard(Main.currentUser());});
            taskMenuButton.getItems().addAll(item1, item2);
        } else if (role == UserRole.ADMINISTRATOR) {
            MenuItem item1 = new MenuItem("Beheer cursussen");
            item1.setOnAction(e -> {Main.getSceneManager().showManageCoursesScene(Main.currentUser());});
            MenuItem item2 = new MenuItem("Beheer groepen");
            item2.setOnAction(e -> {Main.getSceneManager().showManageGroupsScene(Main.currentUser());});
            MenuItem item3 = new MenuItem("Beheer studenten");//????
            item3.setOnAction(e -> {Main.getSceneManager().showAssignStudentsToGroupScene(Main.currentUser());});
            taskMenuButton.getItems().addAll(item1, item2,item3);
        } else if (role == UserRole.FUNCTIONEEL_BEHEERDER) {
            MenuItem item1 = new MenuItem("Beheer gebruikers");
            item1.setOnAction(e -> {Main.getSceneManager().showManageUsersScene(Main.currentUser());});
            taskMenuButton.getItems().add(item1);
           // item1.getOnAction().handle(null);
        } else if (role == UserRole.DOCENT) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Geen taken");
            alert.setContentText("Er zijin geen taken voor u");
            alert.show();

            PauseTransition screen = new PauseTransition(javafx.util.Duration.seconds(2));
            screen.setOnFinished(e -> Main.getSceneManager().showLoginScene());
            screen.play();

        }
    }
    public void doLogout() {
        Main.getSceneManager().showLoginScene();
    }
}