package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Course;
import model.User;
import model.UserRole;
import view.Main;

import java.util.List;

public class CreateUpdateCourseController {
    @FXML
    private TextField courseName;
    @FXML
    private ComboBox<String> courseLevel;
    @FXML
    private ComboBox<String> coordinator;
    @FXML
    private Label titelLabel;


    private User loggedInUser;
    DBAccess dbAccess = Main.getDBAccess();
    private final UserDAO userDAO = new UserDAO(dbAccess);
    private final CourseDAO courseDAO = new CourseDAO(dbAccess);
    private boolean isUpdate = false;


    public void setup(Course selectedCourse, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        coordinator.getItems().clear();
        List<User> users = userDAO.getAll();
        for (User u : users) {
            if (u.getUserRol() == UserRole.COÖRDINATOR) {
                coordinator.getItems().add(u.getUserName());
            }
        }

        if (selectedCourse != null) {
            isUpdate = true;
            courseName.setText(selectedCourse.getCourseName());
            courseName.setDisable(true);
            courseLevel.setValue(selectedCourse.getCourseLevel());
            coordinator.setValue(selectedCourse.getCoordinator().getUserName());
            titelLabel.setText("Update Course");
        } else {
            isUpdate = false;
            courseName.clear();
            courseName.setDisable(false);
            courseName.setPromptText("Voer de cursusnaam in");
            courseName.setStyle("-fx-prompt-text-fill: gray;");
            courseLevel.getSelectionModel().clearSelection();
            coordinator.getSelectionModel().clearSelection();
            titelLabel.setText("Nieuwe Course");
        }
    }

    @FXML
    public void doMenu() {
        Main.getSceneManager().showManageCoursesScene(loggedInUser);
    }

    @FXML
    public void doCreateUpdateCourse() {
        String name = courseName.getText();
        String level = courseLevel.getValue();
        String coorDinator = coordinator.getValue();

        // Hij moet overal invullen
        if (name == null || name.isBlank() || level == null || coorDinator == null) {
            showAlert(Alert.AlertType.WARNING, "Fout", null, "Vul alle velden in a.u.b.");
            return;
        }

        if (!isUpdate) {
            Course existingCourse = courseDAO.getOneByName(name);
            if (existingCourse != null) {
                showAlert(Alert.AlertType.ERROR, "Fout", null, "Een cursus met deze naam bestaat al.");
                return;
            }
        }

        User user = userDAO.getOneByName(coorDinator);

        Course course = new Course(name, level, user);

        try {
            if (isUpdate) {
                courseDAO.updateCourse(course);
            } else {
                courseDAO.storeOne(course);
            }
            showAlert(Alert.AlertType.INFORMATION, "Bevestiging", null, "Course is opgeslagen.");
            Main.getSceneManager().showManageCoursesScene(loggedInUser);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Fout", null, "Fout bij opslaan van course: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}