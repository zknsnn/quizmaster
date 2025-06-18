package controller;

import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.User;
import model.UserRole;
import view.Main;

import java.util.List;
import java.util.stream.Collectors;

public class CreateUpdateCourseController {

    private User user;

    @FXML
    private ComboBox<User> coordinatorComboBox;

    @FXML
    private ComboBox<String> courseLevelComboBox;

    @FXML
    private TextField coursenameTextfield;


    public void setup() {
    }
    public void doMenu() {
        Main.getSceneManager().showManageCoursesScene(user);
    }

    public void doCreateUpdateCourse() {}
}
