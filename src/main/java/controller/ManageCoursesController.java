package controller;

import database.mysql.CourseDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Course;
import model.User;
import view.Main;

import java.util.List;
import java.util.Optional;

public class ManageCoursesController {

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> courseLevelColumn;

    @FXML
    private TableColumn<Course, String> coordinatorColumn;

    private CourseDAO courseDAO;
    private User loggedInUser;

    public void setup(User user) {
        this.loggedInUser = user;
        this.courseDAO = new CourseDAO(Main.getDBAccess());

        List<Course> courses = courseDAO.getAll();

        courseNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCourseName())
        );

        courseLevelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCourseLevel())
        );

        coordinatorColumn.setCellValueFactory(cellData -> {
            User coordinator = cellData.getValue().getCoordinator();
            String name;
            if (coordinator != null) {
                name = coordinator.getFirstName() + " " + coordinator.getLastName();
            } else {
                name = "Onbekend";
            }
            return new SimpleStringProperty(name);
        });

        courseTable.getItems().setAll(courses);
    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    @FXML
    public void doCreateCourse() {
        Main.getSceneManager().showCreateUpdateCourseScene(null, loggedInUser);
    }

    @FXML
    public void doUpdateCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.ERROR, "Fout bij updaten", "Update is mislukt", "Selecteer een cursus om te updaten.");
        } else {
            Main.getSceneManager().showCreateUpdateCourseScene(selectedCourse, loggedInUser);
        }
    }

    @FXML
    public void doDeleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.ERROR, "Fout bij verwijderen", "Verwijderen mislukt", "Selecteer een cursus om te verwijderen.");
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Bevestiging");
            confirm.setHeaderText("Wil je de cursus '" + selectedCourse.getCourseName() + "' verwijderen?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                courseDAO.deleteCourse(selectedCourse.getCourseName());
                courseTable.getItems().remove(selectedCourse);
                showAlert(Alert.AlertType.INFORMATION, "Bevestiging", null, "Cursus '" + selectedCourse.getCourseName() + "' is verwijderd.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}