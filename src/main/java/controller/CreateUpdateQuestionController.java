package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;


public class CreateUpdateQuestionController {
    private final QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());
    private User loggedInUser;
    private QuizDAO quizDAO;
    private Question question;
    private Question selectedQuestion;
    private Question questionId;

    @FXML
    private Label titelLabel;
    @FXML
    private TextField quizNaamTextField;
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
        if (selectedQuestion == null) {
            createQuestion();
        } else {
            updateQuestion();
        }
//
//        if(currentquestion != null){
//            vulalleveldenmetdeinhoud
//        }

    } // end doCreateUpdateQuestion

    private Question updateQuestion() {
        haalInformatieQuestionOp(selectedQuestion);
        // Haal questionId op
        Stage primaryStage = null;
        questionId = questionDAO.getOneById(questionId.getQuestionId());
        String quizNaam = quizNaamTextField.getText();
        Quiz quiz = quizDAO.getQuizPerID(quizNaam);
        // Gegevens bijwerken
        question.setQuestionText(selectedQuestion.getQuestionText());
        question.setCorrectAnswer(selectedQuestion.getCorrectAnswer());
        question.setWrongAnswer1(selectedQuestion.getWrongAnswer1());
        question.setWrongAnswer2(selectedQuestion.getWrongAnswer2());
        question.setWrongAnswer3(selectedQuestion.getWrongAnswer3());
        questionDAO.updateQuestion(question);
        showAutoClosingWarning(primaryStage, "Vraag bijgewerkt.");
        return question;
    } // updateQuestion

    private Question createQuestion() {
        String questionText = questionNaamTextfield.getText();
        String correctAnswer = correctAnswerTextfield.getText();
        String wrongAnswer1 = wrongAnswer1Textfield.getText();
        String wrongAnswer2 = wrongAnswer2Textfield.getText();
        String wrongAnswer3 = wrongAnswer3Textfield.getText();
        String quizNaam = quizNaamTextField.getText();
        Quiz quiz = quizDAO.getQuizPerID(quizNaam);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Opslaan");
        alert.setHeaderText("Opslaan niet gelukt!");

        if (questionText.isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Voer een vraag in.");
            return null;
        }
        if (questionText.trim().isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Alleen spaties als vraag is niet toegestaan.");
            return null;
        }
        if (correctAnswer.isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Voer een correct antwoord in.");
            return null;
        }
        if (correctAnswer.trim().isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Alleen spaties als correct antwoord is niet toegestaan.");
            return null;
        }
        if (wrongAnswer1.isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Voer een eerste fout antwoord in.");
            return null;
        }
        if (wrongAnswer1.trim().isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Alleen spaties als eerste fout antwoord is niet toegestaan.");
            return null;
        }
        if (wrongAnswer2.isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Voer een tweede fout antwoord in.");
            return null;
        }
        if (wrongAnswer2.trim().isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Alleen spaties als tweede fout antwoord is niet toegestaan.");
            return null;
        }
        if (wrongAnswer3.isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Voer een derde fout antwoord in.");
            return null;
        }
        if (wrongAnswer3.trim().isEmpty()) {
            Stage primaryStage = null;
            showAutoClosingWarning(primaryStage,"Alleen spaties als derde fout antwoord is niet toegestaan.");
            return null;
        }
        return new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz);
    } // end createQuestion

    private void haalInformatieQuestionOp(Question question) {
        titelLabel.setText("Wijzig question");
        quizNaamTextField.setText(quizNaamTextField.getSelectedText());
        questionNaamTextfield.setText(selectedQuestion.getQuestionText());
        correctAnswerTextfield.setText(selectedQuestion.getCorrectAnswer());
        wrongAnswer1Textfield.setText(selectedQuestion.getWrongAnswer1());
        wrongAnswer2Textfield.setText(selectedQuestion.getWrongAnswer2());
        wrongAnswer3Textfield.setText(selectedQuestion.getWrongAnswer3());
    } // haalInformatieQuestionOp

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    } // end showAlert

    private void showAutoClosingWarning(Stage owner, String message) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1px;");
        popup.getContent().add(label);

        // Toon popup gecentreerd op het hoofdvenster
        popup.show(owner);
        popup.setX(owner.getX() + owner.getWidth() / 2 - label.getWidth() / 2);
        popup.setY(owner.getY() + owner.getHeight() / 2 - label.getHeight() / 2);

        // Verberg na 2 seconden
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    } // showAutoClosingWarning

} // CreateUpdateQuestionController