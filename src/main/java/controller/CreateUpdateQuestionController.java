package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

public class CreateUpdateQuestionController {
    private final QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());
    private User loggedInUser;
    private QuizDAO quizDAO;
    private Question selectedQuestion;

    @FXML
    private Label titelLabel;
//    @FXML
//    private ComboBox<String> quizNaamComboBox;
    @FXML
    private TextField questionNaamTextfield;
    @FXML
    private TextField correctAnswerTextfield;
    @FXML
    private TextField wrongAnswer1Textfield;
    @FXML
    private TextField wrongAnswer2Textfield;
    @FXML
    private TextField wrongAnswer3Textfield;

    public void setup(Question question) {
        this.loggedInUser = loggedInUser;
        this.quizDAO = new QuizDAO(Main.getDBAccess());

        if (question != null) {
            this.selectedQuestion = question;
            haalInformatieQuestionOp(question);
        } else {
            this.selectedQuestion = null;
        }
    } // end setup

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doCreateUpdateQuestion() {
        Question question = createQuestion();
        boolean correcteInvoer = true;
        String questionNaam = questionNaamTextfield.getText();
//
//        if(currentquestion != null){
//            vulalleveldenmetdeinhoud
//        }

    } // end doCreateUpdateQuestion

    private void haalInformatieQuestionOp(Question question) {
        titelLabel.setText("Wijzig question");
//        quizNaamComboBox.setCellFactory();
        questionNaamTextfield.setText(selectedQuestion.getQuestionText());
        correctAnswerTextfield.setText(selectedQuestion.getCorrectAnswer());
        wrongAnswer1Textfield.setText(selectedQuestion.getWrongAnswer1());
        wrongAnswer2Textfield.setText(selectedQuestion.getWrongAnswer2());
        wrongAnswer3Textfield.setText(selectedQuestion.getWrongAnswer3());
    } // haalInformatieQuestionOp

    private Question createQuestion() {
        return (null);
    } // end createQuiz

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    } // end showAlert
} // CreateUpdateQuestionController