package controller;

import database.mysql.CourseDAO;
import database.mysql.QuizDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Course;
import model.Quiz;
import model.User;
import view.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateUpdateQuizController implements Initializable {
    private User loggedInUser;
    private QuizDAO quizDAO;
    private CourseDAO courseDAO;
    private Quiz selectedQuiz;

    @FXML
    private Label titelLabel;

    @FXML
    private TextField quizNaamTextfield;

    @FXML
    private TextField quizSuccesDefinitieTextfield;

    @FXML
    private ComboBox<String> quizLevelComboBox;

    @FXML
    private ComboBox<Course> quizCursusCombobox;

    public void setup(Quiz quiz, User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        this.courseDAO = new CourseDAO(Main.getDBAccess());
        this.selectedQuiz = quiz;

        haalInformatieQuizOp(quiz);

        fillComboBosx();

    } // einde setup

    public void doMenu() {
        Main.getSceneManager().showManageQuizScene(loggedInUser);
    }

    public void dashboardMenu(){ Main.getSceneManager().showCoordinatorDashboard(loggedInUser);}

    public void doCreateUpdateQuiz() {
        Quiz quiz = createQuiz();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Opslaan");
        if (quiz != null) {
            if (selectedQuiz == null) {
                quizDAO.saveQuiz(quiz);
                alert.setHeaderText("Quiz opgeslagen");
                alert.setContentText("Quiz " + quiz.getQuizName() + " is opgeslagen.");
                alert.showAndWait();
                clearFields();
            } else {
                quiz.setQuizName(quiz.getQuizName());
                quizDAO.updateQuiz(quiz);
                alert.setHeaderText("Quiz gewijzigd");
                alert.setContentText("Quiz " + quiz.getQuizName() + " is gewijzigd.");
                alert.showAndWait();
                Main.getSceneManager().showManageQuizScene(loggedInUser);
            }
        }
    } // einde doCreateUpdateQuiz

    private Quiz createQuiz() {
        String quizNaam = quizNaamTextfield.getText();
        String quizLevel = quizLevelComboBox.getSelectionModel().getSelectedItem();
        double succesDefinitie;
        Course course = quizCursusCombobox.getValue();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Opslaan");
        alert.setHeaderText("Opslaan onsuccesvol");

        if (controleerNaamQuiz(quizNaam, alert)) return null;

        try {
                succesDefinitie = Double.parseDouble(quizSuccesDefinitieTextfield.getText());
                if (succesDefinitie < 0 || succesDefinitie > 100) {
                    alert.setContentText("Voer een succesdefinitie in tussen de 0 en 100%.");
                    alert.showAndWait();
                    return null;
                }
            } catch (NumberFormatException e) {
                alert.setContentText("De succesdefinitie moet een numerieke waarde zijn (bijv. 75.5 of 80).");
                alert.showAndWait();
                return null;
            }

        if (controleerQuizNiveau(quizLevel, alert, course)) return null;

        return new Quiz(quizNaam, quizLevel, succesDefinitie, course);
    } // einde createQuiz

    private static boolean controleerQuizNiveau(String quizLevel, Alert alert, Course course) {
        if (quizLevel == null || quizLevel.isEmpty()) {
            alert.setContentText("Selecteer een quizniveau.");
            alert.showAndWait();
            return true;
        }
        if ((course.getCourseLevel().equalsIgnoreCase("Beginner") && (quizLevel.equalsIgnoreCase
                ("Medium") || quizLevel.equalsIgnoreCase("Gevorderd")))) {
            alert.setContentText("Course niveau is Beginner. Selecteer Beginner als quizniveau.");
            alert.showAndWait();
            return true;
        }
        if ((course.getCourseLevel().equalsIgnoreCase("Medium") && quizLevel.equalsIgnoreCase
                ("Gevorderd"))) {
            alert.setContentText("Course niveau is Medium. Selecteer Beginner of Medium als quizniveau.");
            alert.showAndWait();
            return true;
        }
        if ((course.getCourseLevel().equalsIgnoreCase("Gevorderd") && quizLevel.equalsIgnoreCase
                ("Beginner"))) {
            alert.setContentText("Course niveau is Gevorderd. Selecteer Medium of Gevorderd als quizniveau.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private static boolean controleerNaamQuiz(String quizNaam, Alert alert) {
        if (quizNaam.isEmpty()) {
            alert.setContentText("Voer een quiznaam in.");
            alert.showAndWait();
            return true;
        }
        if (quizNaam.trim().isEmpty()) {
            alert.setContentText("Alleen spaties als naam is niet toegestaan.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    // Met deze methode worden alle tekstvelden leeggehaald.
    private void clearFields() {
        quizNaamTextfield.clear();
        quizLevelComboBox.getSelectionModel().selectFirst();
        quizSuccesDefinitieTextfield.clear();
        quizCursusCombobox.getSelectionModel().selectFirst();
    } // einde clearFields

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> levels = FXCollections.observableArrayList("Beginner", "Medium", "Gevorderd");
        quizLevelComboBox.setItems(levels);
        quizLevelComboBox.getSelectionModel().selectFirst();
    }

    private void fillComboBosx() {
        List<Course> cursussen = courseDAO.getCoursePerCoordinator(loggedInUser);
        for (Course course : cursussen) {
            quizCursusCombobox.getItems().add(course);
        }
        quizCursusCombobox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course obj) {
                if (obj != null) {
                    return obj.getCourseName();
                }
                return "";
            }
            @Override
            public Course fromString(String string) {
                return null;
            }
        });
    }

    private void haalInformatieQuizOp(Quiz quiz) {
        titelLabel.setText("Wijzig quiz");
        quizNaamTextfield.setText(quiz.getQuizName());
        quizNaamTextfield.setDisable(true);
        quizLevelComboBox.getSelectionModel().select(quiz.getQuizLevel());
        quizSuccesDefinitieTextfield.setText(String.valueOf(quiz.getSuccesDefinition()));
        quizCursusCombobox.setValue(selectedQuiz.getCourse());
    }
}