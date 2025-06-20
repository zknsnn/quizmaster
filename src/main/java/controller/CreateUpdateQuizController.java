package controller;

import database.mysql.QuizDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Quiz;
import model.User;
import view.Main;

public class CreateUpdateQuizController {
    private User loggedInUser;
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

    @FXML
    private TextField quizAantalVragenTextField;

    //    Quizzen CRUD Als coördinator wil ik de volledige CRUD-functionaliteit voor het beheer van
    //    quizzen hebben behorende bij cursussen waarvoor ik de rol coördinator heb (schermen
    //    manageQuizzes.fxml, createUpdateQuiz.fxml). Als ik een quiz selecteer (scherm
    //    manageQuizzes.fxml) wil ik meteen kunnen zien hoeveel vragen er in de quiz zitten.
    //    public CreateUpdateQuizController(ComboBox quizLevelComboBox) {
//        this.quizLevelComboBox = quizLevelComboBox;
//    }

    public void setup(Quiz quiz, User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());

        if (quiz != null) {
            titelLabel.setText("Wijzig quiz");
            quizNaamTextfield.setText(String.valueOf(quiz.getQuizName()));
            quizLevelTextfield.setText(String.valueOf(quiz.getQuizLevel()));
            quizSuccesDefinitieTextfield.setText(String.valueOf(quiz.getSuccesDefinition()));
            quizCursusTextField.setText(String.valueOf(quiz.getCourse().getCourseName()));
            quizAantalVragenTextField.setText(String.valueOf(quiz.telAantalVragen(quiz)));
        }
//        if (quiz == null) {
//            titelLabel.setText("Nieuwe quiz");
//        }
        // ik moet ook nog een methode toevoegen die het aantal vragen telt.
    } // einde setup

    public void doMenu() {
        Main.getSceneManager().showManageQuizScene(loggedInUser);
    }

    public void doCreateUpdateQuiz() {
    }
}