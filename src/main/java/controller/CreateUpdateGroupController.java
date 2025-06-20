package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.GroupDAO;
import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.Course;
import model.Group;
import model.User;
import model.UserRole;
import view.Main;
import javafx.scene.control.TextField;

//import java.awt.*;
//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;
import java.util.List;

public class CreateUpdateGroupController {

    DBAccess dbAccess = Main.getDBAccess();
    private final GroupDAO groupDAO = new GroupDAO(dbAccess);
    private final CourseDAO courseDAO = new CourseDAO(dbAccess);
    private final UserDAO userDAO = new UserDAO(dbAccess);
    @FXML
    private TextField groupName;
    @FXML
    private ComboBox<String> course;
    @FXML
    private TextField numberOfStudents;
    @FXML
    private ComboBox<String> teacher;
    @FXML
    private Label titelLabel;

    public void setup(Group group) {
        List<Course> courses = courseDAO.getAll();
        List<User> users = userDAO.getAll();
        for (User u:users){
            if (u.getUserRol() == UserRole.DOCENT){
                teacher.getItems().add(u.getUserName());
            }
        }
        for (Course c:courses){
            course.getItems().add(c.getCourseName());
        }
        if (group != null){
            groupName.setText(group.getGroupName());
            numberOfStudents.setText(String.valueOf(group.getAmount()));
            course.setValue(group.getCourse().getCourseName());
            teacher.setValue(group.getDocent().getUserName());
            titelLabel.setText("Groep Update");
        }else {
            groupName.clear();
            course.getSelectionModel().clearSelection();
            numberOfStudents.clear();
            teacher.getSelectionModel().clearSelection();
        }
    }
    @FXML
    public void doMenu(ActionEvent event) {Main.getSceneManager().showManageGroupsScene(Main.currentUser());}
    @FXML
    public void doCreateUpdateGroup(ActionEvent event) {}
}
