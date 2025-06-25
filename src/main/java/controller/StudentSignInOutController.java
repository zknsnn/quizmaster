package controller;

import database.mysql.CourseDAO;
import database.mysql.InschrijvingDAO;
import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.Course;
import model.Inschrijving;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class StudentSignInOutController {

    private InschrijvingDAO inschrijvingDAO;
    private CourseDAO courseDAO;
    private User loggedInUser;
    @FXML
    private ListView<Course> signedOutCourseList;
    @FXML
    private ListView<Course> signedInCourseList;

//    Initialiseer de controller met de ingelogde gebruiker.
//    Laadt de lijsten van ingeschreven en niet-ingeschreven cursussen.

    public void setup(User user) {
        this.loggedInUser = user;
        this.inschrijvingDAO = new InschrijvingDAO(Main.getDBAccess());
        this.courseDAO = new CourseDAO(Main.getDBAccess());

        // meer courses selecteren
        signedOutCourseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        signedInCourseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        signedOutCourseList.getItems().clear();
        signedInCourseList.getItems().clear();

        List<Course> courses = courseDAO.getAll();
        List<Inschrijving> inschrijvingen = inschrijvingDAO.getInschrijvingByStudentname(user.getUserName());

        List<Course> signedInCourses = new ArrayList<>();
        List<Course> notSignedInCourses = new ArrayList<>();

        // courses namen halen van Inschrijving
        List<String> signedCourseNames = new ArrayList<>();
        for (Inschrijving inschrijving : inschrijvingen) {
            signedCourseNames.add(inschrijving.getCourse().getCourseName());
        }

        // controller of user ingeschreven is.
        for (Course course : courses) {
            if (signedCourseNames.contains(course.getCourseName())) {
                signedInCourses.add(course);
            } else {
                notSignedInCourses.add(course);
            }
        }

        signedOutCourseList.getItems().addAll(notSignedInCourses);
        signedInCourseList.getItems().addAll(signedInCourses);

    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doSignIn() {
        List<Course> selectedCourses = signedOutCourseList.getSelectionModel().getSelectedItems();

        if (selectedCourses == null || selectedCourses.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Inschrijven is mislukt");
            alert.setContentText("Selecteer een of meerdere cursussen om in te schrijven.");
            alert.showAndWait();
        } else {
            for (Course course : selectedCourses) {
                Inschrijving inschrijving = new Inschrijving(loggedInUser, course);
                inschrijvingDAO.storeOne(inschrijving);
            }
            setup(loggedInUser); // refresh list
        }
    }

    public void doSignOut() {
        List<Course> selectedCourses = signedInCourseList.getSelectionModel().getSelectedItems();

        if (selectedCourses == null || selectedCourses.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Uitschrijven is mislukt");
            alert.setContentText("Selecteer een of meerdere cursussen om uit te schrijven.");
            alert.showAndWait();
        } else {
            for (Course course : selectedCourses) {
                inschrijvingDAO.deleteInschrijving(course.getCourseName());
            }
            setup(loggedInUser);
        }
    }

}
