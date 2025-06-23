package controller;

import database.mysql.CourseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Course;
import model.User;
import view.Main;

import java.util.List;

public class ManageCoursesController {


    @FXML
    ListView<Course> courseList;

    private CourseDAO courseDAO;
    private User loggedInUser;


    public void setup(User user) {
        this.loggedInUser = user;
        this.courseDAO = new CourseDAO(Main.getDBAccess());
        courseList.getItems().clear();
        List<Course> courses = courseDAO.getAll();
        courseList.getItems().addAll(courses);
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doCreateCourse() {
        Main.getSceneManager().showCreateUpdateCourseScene(null,loggedInUser);
    }

    public void doUpdateCourse() {
        Course selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij updaten");
            alert.setHeaderText("Update is mislukt");
            alert.setContentText("Selecteer een cursus om te updaten.");
            alert.showAndWait();
        }else {
            Main.getSceneManager().showCreateUpdateCourseScene(selectedCourse,loggedInUser);
        }
    }

    public void doDeleteCourse() {
        Course selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij verwijderen");
            alert.setHeaderText("Verwijderen mislukt");
            alert.setContentText("Selecteer een cursus om te verwijderen.");
            alert.showAndWait();
            return;
        }
        courseDAO.deleteCourse(selectedCourse.getCourseName());
        courseList.getItems().remove(selectedCourse);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bevestiging");
        alert.setHeaderText(null);
        alert.setContentText("Course "+ selectedCourse.getCourseName() + " is verwijderd");
        alert.showAndWait();
    }
}
