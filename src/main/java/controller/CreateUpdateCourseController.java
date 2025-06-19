package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Course;
import model.User;
import model.UserRole;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class CreateUpdateCourseController {
    @FXML
    private TextField courseName;
    @FXML
    private ComboBox<String> courseLevel;
    @FXML
    private ComboBox<String> coordinator;

    private User user;
    DBAccess dbAccess = Main.getDBAccess();
    private final UserDAO userDAO = new UserDAO(dbAccess);
    private final CourseDAO courseDAO = new CourseDAO(dbAccess);


    public void setup(Course selectedCourse) {

        if (selectedCourse != null) {
            courseName.setText(selectedCourse.getCourseName());
            courseLevel.setValue(selectedCourse.getCourseLevel());
        } else {
            courseName.clear();
            courseLevel.getSelectionModel().clearSelection();

        }
        List<User> users = userDAO.getAll();
        for (User u : users) {
            if (u.getUserRol() == UserRole.COÖRDINATOR) {
                coordinator.getItems().add(u.getUserName());
            }
        }
    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showManageCoursesScene(user);
    }
    @FXML
    public void doCreateUpdateCourse() {}
}