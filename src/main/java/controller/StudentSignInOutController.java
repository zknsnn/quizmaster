package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.InschrijvingDAO;
import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Course;
import model.Inschrijving;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class StudentSignInOutController {

    private InschrijvingDAO inschrijvingDAO;
    private CourseDAO courseDAO;
    private UserDAO userDAO;
    private User loggedInUser;
    @FXML
    private ListView<Course> signedOutCourseList;
    @FXML
    private ListView<Course> signedInCourseList;

    public void setup(User user) {
        this.loggedInUser = user;
        this.inschrijvingDAO = new InschrijvingDAO(Main.getDBAccess());
        this.courseDAO = new CourseDAO(Main.getDBAccess());

        signedOutCourseList.getItems().clear();
        signedInCourseList.getItems().clear();

        // alle coursessen ophalen
        List<Course> courses = courseDAO.getAll();
        // als die student ingechreven is, ophalen
        List<Inschrijving> inschrijvingen = inschrijvingDAO.getInschrijvingByStudentname(user.getUserName());

        // welke courses heeft hij ingeschreven
        List<Course> signedInCourses = new ArrayList<>();
        for (Inschrijving i : inschrijvingen) {
            signedInCourses.add(i.getCourse());
        }

        // welke courses heeft hij niet ingeschreven
        List<Course> notSignedInCourses = new ArrayList<>();
        for (Course course : courses) {
            boolean isSignedIn = false;
            for (Course signedIn : signedInCourses) {
                if (course.getCourseName().equals(signedIn.getCourseName())) {
                    isSignedIn = true;
                    break;
                }
            }
            if (!isSignedIn) {
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
        Course selectedCourse = signedOutCourseList.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Inschrijven is mislukt");
            alert.setContentText("Selecteer een cursus om in te schrijven.");
            alert.showAndWait();
        }else {
            Inschrijving inschrijving = new Inschrijving(loggedInUser,selectedCourse);
            inschrijvingDAO.storeOne(inschrijving);
            setup(loggedInUser);
        }
    }

    public void doSignOut() {
        Course selectedCourse = signedInCourseList.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Uitschrijven is mislukt");
            alert.setContentText("Selecteer een cursus om uit te schrijven.");
            alert.showAndWait();
        }else {

            inschrijvingDAO.deleteInschrijving(selectedCourse.getCourseName());
            setup(loggedInUser);
        }
    }
}
