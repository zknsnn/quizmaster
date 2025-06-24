package controller;

import database.mysql.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.Course;
import model.Group;
import model.Inschrijving;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class AssignStudentsToGroupController {
    private User user;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private InschrijvingDAO inschrijvingDAO;
    List<Course> allCourses;
    List<Group> allGroups;
    List<Inschrijving> selectedCourseEnrolledStudents;
    List<Group> selectedCourseGroups;
    @FXML
    ComboBox<String> courseComboBox;
    @FXML
    ComboBox<String> groupComboBox;
    @FXML
    ListView<String> studentList;
    @FXML
    ListView<User> studentsInGroupList;

    public void setup(User currentUser) {
        this.user = currentUser;
        this.courseDAO = new CourseDAO(Main.getDBAccess());
        this.groupDAO = new GroupDAO(Main.getDBAccess());
        allCourses = courseDAO.getAll();
        for (Course c:allCourses){
            courseComboBox.getItems().add(c.getCourseName());
        }
//        allGroups = groupDAO.getAll();
//        String selectedCourse = courseComboBox.getValue();
//        for (Group g:allGroups){
//            if (g.getCourse().getCourseName().equals(selectedCourse)){
//                groupComboBox.getItems().add(g.getGroupName());
//            }
//        }


        courseComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldCourse, newCourse) ->
                {
                    groupComboBox.getItems().clear();
                    System.out.println("Geselecteerde cursus: " + observableValue + ", " + oldCourse + ", " + newCourse);
                    if (newCourse != null){
                        allGroups = groupDAO.getAll();
                        for (Group g:allGroups){
                            if (newCourse.equals(g.getCourse().getCourseName())){
                                groupComboBox.getItems().add(g.getGroupName());
                            }
                        }
                        selectedCourseEnrolledStudents = inschrijvingDAO.getInschrijvingByCoursename(newCourse);
                        for (Inschrijving i:selectedCourseEnrolledStudents){
                            studentList.getItems().add(i.getStudent().getUserName());
                        }

                    }
                });
        groupComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldGroup, newGroup) ->
                        System.out.println("Geselecteerde groep: " + observableValue + ", " + oldGroup + ", " + newGroup));
    }

    public void doAssign() {}

    public void doRemove() {}

    public void doMenu() {Main.getSceneManager().showWelcomeScene(user);}
}
