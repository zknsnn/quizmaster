package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.GroupDAO;
import database.mysql.UserDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
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
    private User user;
    private Group editingGroup;
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

    public void setup(Group group, User currentUser) {
        this.user = currentUser;
        this.editingGroup = group;
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
            groupName.setDisable(true);
            numberOfStudents.setText(String.valueOf(group.getAmount()));
            course.setValue(group.getCourse().getCourseName());
            course.setDisable(true);
            teacher.setValue(group.getDocent().getUserName());
            titelLabel.setText("Groep Update");
        }else {
            groupName.clear();
            course.getSelectionModel().clearSelection();
            course.setPromptText("Selecteer cursus");
            numberOfStudents.clear();
            numberOfStudents.setPromptText("");
            teacher.getSelectionModel().clearSelection();
            teacher.setPromptText("Selecteer docent");
        }
    }
    @FXML
    public void doMenu(ActionEvent event) {Main.getSceneManager().showManageGroupsScene(Main.currentUser());}
    @FXML
    public void doCreateUpdateGroup(ActionEvent event) {
        String name = groupName.getText();
        String selectedCourseName = course.getValue();
        String selectedTeacherName = teacher.getValue();
        String amountStudentText = numberOfStudents.getText();

        if (name.isEmpty() || selectedCourseName == null || selectedTeacherName == null || amountStudentText.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Warning", "Vul alle velden in.");
            return;
        }

        int amountStudent;
        try{
            amountStudent = Integer.parseInt(amountStudentText);
        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR,"Fout", "Aantal studenten moet een geldig getal zijn.");
            return;
        }

        if(amountStudent < 0 || amountStudent > 25){
            showAlert(Alert.AlertType.WARNING, "Let op","Aantal studenten moet tussen 1 en 25 zijn.");
            return;
        }

        Course selectedCourse = courseDAO.getOneByName(selectedCourseName);
        User selectedTeacher = userDAO.getOneByName(selectedTeacherName);

        if (editingGroup == null){
            // Nieuwe groep toevoegen
            Group existingGroup = groupDAO.getOneByNameAndCourseName(name,selectedCourseName);
            if (existingGroup != null){
                showAlert(Alert.AlertType.ERROR, "Fout", name + " bestaat al voor " + selectedCourseName + " cursus.");
                return;
            }else {
                Group newGroup = new Group(selectedCourse,name,amountStudent,selectedTeacher);
                groupDAO.storeOne(newGroup);
                showAlert(Alert.AlertType.INFORMATION, "Bevestiging", name + " toegevoegd.");
            }
        }else {
            //Bestaande groep updated
            editingGroup.setGroupName(name);
            editingGroup.setCourse(selectedCourse);
            editingGroup.setAmount(amountStudent);
            editingGroup.setDocent(selectedTeacher);
            groupDAO.updateOne(editingGroup);
            showAlert(Alert.AlertType.INFORMATION, "Bevestiging", name +" is bijgewerkt.");
        }
        Main.getSceneManager().showManageGroupsScene(user);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
