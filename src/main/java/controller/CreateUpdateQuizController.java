package controller;

import database.mysql.QuizDAO;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Quiz;
import model.User;
import view.Main;

public class CreateUpdateQuizController {
    private User user;
    private QuizDAO quizDAO;

    @FXML
    private Label titelLabel;

    @FXML
    private TextField quizNaamTextfield;

    @FXML
    private TextField quizLevelTextfield;

    @FXML
    private TextField quizSuccesDefinitieTextfield;

    @FXML
    private TextField quizCursusTextField;




    //    Quizzen CRUD Als coördinator wil ik de volledige CRUD-functionaliteit voor het beheer van
    //    quizzen hebben behorende bij cursussen waarvoor ik de rol coördinator heb (schermen
    //    manageQuizzes.fxml, createUpdateQuiz.fxml). Als ik een quiz selecteer (scherm
    //    manageQuizzes.fxml) wil ik meteen kunnen zien hoeveel vragen er in de quiz zitten.

    //    public CreateUpdateQuizController(ComboBox quizLevelComboBox) {
//        this.quizLevelComboBox = quizLevelComboBox;

//    }
    public void setup(Quiz quiz) {
        this.user = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        titelLabel.setText("Wijzig klant");
        quizNaamTextfield.setText(String.valueOf(quiz.getQuizName()));
        quizLevelTextfield.setText(String.valueOf(quiz.getQuizLevel()));
        quizSuccesDefinitieTextfield.setText(String.valueOf(quiz.getSuccesDefinition()));
        quizCursusTextField.setText(String.valueOf(quiz.getCourse().getCourseName()));
        // ik moet ook nog een methode toevoegen die het aantal vragen telt.
    }

    public void doMenu(ActionEvent actionEvent) {
        Main.getSceneManager().showWelcomeScene(user);
    }

    public void doCreateUpdateQuiz() {
    }
}