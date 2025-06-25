package controller;

import database.mysql.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.*;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class AssignStudentsToGroupController {
    private User user;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private UserDAO userDAO;
    private InschrijvingDAO inschrijvingDAO;
    private GroepsIndelingDAO groepsIndelingDAO;
    private String selectedCourseName;
    private String selectedGroupName;
    List<Course> allCourses;
    List<Group> allGroupBySelectedCourse;
    List<User> unassignedStudentsForCourse;
    List<User> selectedGroupStudents;
    @FXML
    ComboBox<String> courseComboBox;
    @FXML
    ComboBox<String> groupComboBox;
    @FXML
    ListView<String> studentList;
    @FXML
    ListView<String> studentsInGroupList;

    public void setup(User currentUser) {
        this.user = currentUser;
        this.courseDAO = new CourseDAO(Main.getDBAccess());
        this.groupDAO = new GroupDAO(Main.getDBAccess());
        this.userDAO = new UserDAO(Main.getDBAccess());
        this.inschrijvingDAO = new InschrijvingDAO(Main.getDBAccess());
        this.groepsIndelingDAO = new GroepsIndelingDAO(Main.getDBAccess());
        allCourses = courseDAO.getAll();

        courseComboBox.setPromptText("Selecteer een cursus");

        for (Course c:allCourses){
            courseComboBox.getItems().add(c.getCourseName());
        }

        studentList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentsInGroupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//        allGroups = groupDAO.getAll();
//        String selectedCourse = courseComboBox.getValue();
//        for (Group g:allGroups){
//            if (g.getCourse().getCourseName().equals(selectedCourse)){
//                groupComboBox.getItems().add(g.getGroupName());
//            }
//        }


        courseComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue1, oldCourse, newCourse) ->
                {
                    groupComboBox.getItems().clear();
                    System.out.println("Geselecteerde cursus: " + observableValue1 + ", " + oldCourse + ", " + newCourse);
                    if (newCourse != null){
                        selectedCourseName = newCourse;
                        allGroupBySelectedCourse = groupDAO.getGroupByCourseName(newCourse);
                        for (Group g:allGroupBySelectedCourse){
                            groupComboBox.getItems().add(g.getGroupName());
                        }
                        unassignedStudentsForCourse = inschrijvingDAO.getUnassignedStudentsForCourse(newCourse);
                        studentList.getItems().clear();
                        for (User u:unassignedStudentsForCourse){
                            studentList.getItems().add(u.getUserName());
                        }

                        groupComboBox.getSelectionModel().selectedItemProperty().addListener(
                                (observableValue2, oldGroup, newGroup) ->{
                                    System.out.println("Geselecteerde groep: " + observableValue2 + ", " + oldGroup + ", " + newGroup);
                                    selectedGroupName = newGroup;
                                    selectedGroupStudents = groepsIndelingDAO.getAllByCourseNameAndGroupName(newGroup,newCourse);
                                    studentsInGroupList.getItems().clear();
                                    for (User u:selectedGroupStudents){
                                        studentsInGroupList.getItems().add(u.getUserName());
                                    }
                                });
                    }
                });

    }

    public void doAssign() {
        List<String> selectedStudentsForAssignGroep = studentList.getSelectionModel().getSelectedItems();
        if (selectedStudentsForAssignGroep == null || selectedStudentsForAssignGroep.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Toevoegen is mislukt");
            alert.setContentText("Selecteer een of meerdere studenten om toe te voegen.");
            alert.showAndWait();
        } else if (selectedGroupName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Toevoegen is mislukt");
            alert.setContentText("Selecteer een groep om toe te voegen.");
            alert.showAndWait();
        } else {
            Group selectedGroup = groupDAO.getOneByNameAndCourseName(selectedGroupName,selectedCourseName);
            User student;
            GroepsIndeling groepsIndeling;
            for (String s:selectedStudentsForAssignGroep) {
                student = userDAO.getOneByName(s);
                groepsIndeling = new GroepsIndeling(selectedGroup,student);
                groepsIndelingDAO.storeOne(groepsIndeling);
            }
            refreshListViews();
        }
    }

    public void doRemove() {
        List<String> selectedStudentsForVerwijderen = studentsInGroupList.getSelectionModel().getSelectedItems();
        if (selectedStudentsForVerwijderen == null || selectedStudentsForVerwijderen.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij selecteren");
            alert.setHeaderText("Toevoegen is mislukt");
            alert.setContentText("Selecteer een of meerdere studenten om te verwijderen.");
            alert.showAndWait();
        }else{
            Group selectedGroup = groupDAO.getOneByNameAndCourseName(selectedGroupName,selectedCourseName);
            User student;
            GroepsIndeling groepsIndeling;
            for (String s:selectedStudentsForVerwijderen) {
                student = userDAO.getOneByName(s);
                groepsIndeling = new GroepsIndeling(selectedGroup,student);
                groepsIndelingDAO.deleteOne(groepsIndeling);
            }
            refreshListViews();
        }
    }

    public void refreshListViews(){
        unassignedStudentsForCourse = inschrijvingDAO.getUnassignedStudentsForCourse(selectedCourseName);
        studentList.getItems().clear();
        for (User u : unassignedStudentsForCourse) {
            studentList.getItems().add(u.getUserName());
        }
        selectedGroupStudents = groepsIndelingDAO.getAllByCourseNameAndGroupName(selectedGroupName, selectedCourseName);
        studentsInGroupList.getItems().clear();
        for (User u : selectedGroupStudents) {
            studentsInGroupList.getItems().add(u.getUserName());
        }
    }

    public void doMenu() {Main.getSceneManager().showWelcomeScene(user);}
}
