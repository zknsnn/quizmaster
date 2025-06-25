package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.Question;
import model.Quiz;
import view.Main;

import java.util.List;


public class CreateUpdateQuestionController {
    private final QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());
    private QuizDAO quizDAO = new QuizDAO(Main.getDBAccess());
    private Question currentQuestion;

    @FXML
    private Label titelLabel;
    @FXML
    private ComboBox<Quiz> quizComboBox;
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
    @FXML
    private Label warningField;

    public void setup(Question question) {
        currentQuestion = question;
        fillComboBox();
        convertQuizToString();
        if (question != null) {
            haalInformatieQuestionOp(question);
        }
    } // end setup

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(Main.currentUser());
    }

    public void doCreateUpdateQuestion() {
        if (checkForEmptyFields()){
            return;
        }
        if (currentQuestion == null) {
            createQuestion();
        } else {
            updateQuestion();
        }
    } // end doCreateUpdateQuestion

    private Question updateQuestion() {
        haalInformatieQuestionOp(currentQuestion);
        String quizNaam = quizComboBox.getSelectionModel().getSelectedItem().getQuizName();
        Quiz quiz = quizDAO.getQuizPerID(quizNaam);
        // Gegevens bijwerken
        currentQuestion.setQuestionText(currentQuestion.getQuestionText());
        currentQuestion.setCorrectAnswer(currentQuestion.getCorrectAnswer());
        currentQuestion.setWrongAnswer1(currentQuestion.getWrongAnswer1());
        currentQuestion.setWrongAnswer2(currentQuestion.getWrongAnswer2());
        currentQuestion.setWrongAnswer3(currentQuestion.getWrongAnswer3());
        questionDAO.updateQuestion(currentQuestion);
        showWarning("Vraag bijgewerkt.");
        return currentQuestion;
    } // updateQuestion

    private Question createQuestion() {
        titelLabel.setText("Nieuwe vraag");
        String questionText = questionNaamTextfield.getText();
        String correctAnswer = correctAnswerTextfield.getText();
        String wrongAnswer1 = wrongAnswer1Textfield.getText();
        String wrongAnswer2 = wrongAnswer2Textfield.getText();
        String wrongAnswer3 = wrongAnswer3Textfield.getText();
        String quizNaam = quizComboBox.getSelectionModel().getSelectedItem().getQuizName();
        Quiz quiz = quizDAO.getQuizPerID(quizNaam);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Opslaan");
        alert.setHeaderText("Opslaan niet gelukt!");


        Question question = new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz);
        questionDAO.storeOne(question);
        showWarning("Nieuwe vraag met antwoorden opgeslagen.");

        return question;
    } // end createQuestion

    private void haalInformatieQuestionOp(Question question) {
        titelLabel.setText("Wijzig vraag");
        quizComboBox.setValue(question.getQuiz());
        questionNaamTextfield.setText(currentQuestion.getQuestionText());
        correctAnswerTextfield.setText(currentQuestion.getCorrectAnswer());
        wrongAnswer1Textfield.setText(currentQuestion.getWrongAnswer1());
        wrongAnswer2Textfield.setText(currentQuestion.getWrongAnswer2());
        wrongAnswer3Textfield.setText(currentQuestion.getWrongAnswer3());
    } // haalInformatieQuestionOp

    // Deze alert geeft een popup met OK button. Niet wenselijk bij onze PO
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    } // end showAlert

    // Alert geleend van Etienne uit User
    // Toont foutmelding tijdelijk
    private void showWarning(String message) {
        warningField.setText(message);
        warningField.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> warningField.setVisible(false));
        pause.play();
    } // showWarning

    public void convertQuizToString() {
        quizComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Quiz quiz) {
                return quiz.getQuizName();
            }

            @Override
            public Quiz fromString(String s) {
                return null;
            }
        });
    } // convertQuizToString

    private boolean checkForEmptyFields() {
        boolean check = false;
        if (questionNaamTextfield.getText() == null || questionNaamTextfield.getText().trim().isEmpty()
                || correctAnswerTextfield.getText() == null || correctAnswerTextfield.getText().trim().isEmpty()
                || wrongAnswer1Textfield.getText() == null || wrongAnswer1Textfield.getText().trim().isEmpty()
                || wrongAnswer2Textfield.getText() == null || wrongAnswer2Textfield.getText().trim().isEmpty()
                || wrongAnswer3Textfield.getText() == null || wrongAnswer3Textfield.getText().trim().isEmpty()) {
            showWarning("Vul alle velden in!");
            check = true;
        }
        return check;
    } // checkForEmptyFields

    public void fillComboBox() {
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        quizComboBox.getItems().addAll(quizzes);
    } // fillComboBox

} // CreateUpdateQuestionController