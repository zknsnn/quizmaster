package controller;

import database.mysql.CourseDAO;
import database.mysql.QuizDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Course;
import model.Quiz;
import model.User;
import view.Main;

import java.net.URL;
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
    private TextField quizCursusTextField;

    @FXML
    private TextField quizAantalVragenTextField;

    @FXML
    private ComboBox<String> quizLevelComboBox;

    //    Quizzen CRUD Als coördinator wil ik de volledige CRUD-functionaliteit voor het beheer van
    //    quizzen hebben behorende bij cursussen waarvoor ik de rol coördinator heb (schermen
    //    manageQuizzes.fxml, createUpdateQuiz.fxml). Als ik een quiz selecteer (scherm
    //    manageQuizzes.fxml) wil ik meteen kunnen zien hoeveel vragen er in de quiz zitten.
    //    public CreateUpdateQuizController(ComboBox quizLevelComboBox) {
    //    this.quizLevelComboBox = quizLevelComboBox;
    //    }

    public void setup(Quiz quiz, User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        this.courseDAO = new CourseDAO(Main.getDBAccess());

        if (quiz != null) {
            this.selectedQuiz = quiz;
            haalInformatieQuizOp(quiz);
        } else {
            this.selectedQuiz = null;
        }
    } // einde setup

    private void haalInformatieQuizOp(Quiz quiz) {
        titelLabel.setText("Wijzig quiz");
        quizNaamTextfield.setText(quiz.getQuizName());
        quizLevelComboBox.getSelectionModel().select(quiz.getQuizLevel());
        quizSuccesDefinitieTextfield.setText(String.valueOf(quiz.getSuccesDefinition()));
        quizCursusTextField.setText(String.valueOf(quiz.getCourse().getCourseName()));
        quizAantalVragenTextField.setText(String.valueOf(quiz.telAantalVragen(quiz)));
    }

    public void doMenu() {
        Main.getSceneManager().showManageQuizScene(loggedInUser);
    }

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
        boolean correcteInvoer = true;
        String quizNaam = quizNaamTextfield.getText();
        String quizLevel = quizLevelComboBox.getSelectionModel().getSelectedItem(); // Haal de geselecteerde String op
        double succesDefinitie = Double.parseDouble(quizSuccesDefinitieTextfield.getText());
        String courseNaam = quizCursusTextField.getText();
        Course course = courseDAO.getOneByName(courseNaam);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Opslaan");
        alert.setHeaderText("Opslaan onsuccesvol");

        if (quizNaam.isEmpty()) {
            correcteInvoer = false;
            alert.setContentText("Voer een quiznaam in.");
            alert.showAndWait();
        }
        if (succesDefinitie < 0 || succesDefinitie > 100) {
            correcteInvoer = false;
            alert.setContentText("Voer een succesdefinitie in tussen de 0 en 100%.");
            alert.showAndWait();
        }
        if (quizLevel == null || quizLevel.isEmpty()) {
            correcteInvoer = false;
            alert.setContentText("Selecteer een quizniveau.");
            alert.showAndWait();
        }
        if (courseNaam.isEmpty()) {
            correcteInvoer = false;
            alert.setContentText("Voer een course in.");
            alert.showAndWait();
        }

        if (correcteInvoer) {
            return new Quiz(quizNaam, quizLevel, succesDefinitie, course);
        } else {
            return null;
        }
    } // einde createQuiz

    // Met deze methode worden alle tekstvelden leeggehaald.
    private void clearFields() {
        quizNaamTextfield.clear();
        quizLevelComboBox.getSelectionModel().selectFirst();
        quizSuccesDefinitieTextfield.clear();
        quizCursusTextField.clear();
        quizAantalVragenTextField.clear();
    } // einde clearFields

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> levels = FXCollections.observableArrayList("Beginner", "Medium", "Gevorderd");
        quizLevelComboBox.setItems(levels);
        quizLevelComboBox.getSelectionModel().selectFirst();
    }
}