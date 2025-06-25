package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuestionsController {
    private QuestionDAO questionDAO;
    private QuizDAO quizDAO;
    private User loggedInUser;
    private Quiz currentQuiz;

    @FXML
    private ListView<Question> questionList;
    @FXML
    private Label quizQuestionsCountLabel;
    @FXML
    private Label warningField;

    public void setup(Quiz quiz, User user) {
        this.loggedInUser = user;
        this.questionDAO = new QuestionDAO(Main.getDBAccess());
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        this.currentQuiz = quiz;
        loadQuestionList();
    }

    @FXML
    public void doMenu(ActionEvent actionEvent) {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    @FXML
    public void doCreateQuestion() {
        Main.getSceneManager().showCreateUpdateQuestionScene(null, currentQuiz);
    }

    @FXML
    public void doUpdateQuestion() {
        Question selectedQuestion = questionList.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Let op !!");
            alert.setHeaderText("Selecteer een vraag.");
            alert.showAndWait();
        } else {
            Main.getSceneManager().showCreateUpdateQuestionScene(selectedQuestion, null);
        }
    }

    @FXML
    public void doDeleteQuestion() {
        Question selectedQuestion = questionList.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            showWarning("Selecteer eerst een vraag om te verwijderen.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijderen");
        alert.setHeaderText("Weet je zeker dat je deze vraag wilt verwijderen?");
        alert.setContentText("Vraag: " + selectedQuestion.getQuestionText() + " ! ");
        questionDAO.deleteQuestion(selectedQuestion);
        loadQuestionList();

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showWarning("Vraag verwijderd.");
            }
        });
    }

//    Als ik een vraag selecteer (scherm manageQuestions.fxml) wil ik meteen kunnen zien
//    hoeveel vragen er in de quiz zitten, waar de geselecteerde vraag toe behoort.

    public void handleQuestionInfo() {
        List<Question> questions = questionDAO.getQuestionsByQuizName(currentQuiz.getQuizName());
        Question selectedQ = questionList.getSelectionModel().getSelectedItem();
        if (selectedQ != null && selectedQ.getQuiz() != null) {
            String selectedQuizName = selectedQ.getQuiz().getQuizName();
            long quizCount = questions.stream().filter(quiz -> selectedQuizName.equals(quiz.getQuiz().getQuizName())).count();
            quizQuestionsCountLabel.setText("Aantal vragen in quiz \"" + selectedQuizName + "\" : " + quizCount);
        } else {
            quizQuestionsCountLabel.setText("Geen quiz gekoppeld aan deze vraag.");
        }
    } // handleQuestionInfo

    // Aanmaken van de lijsten, omdat het soms lang duurt
    private void loadQuestionList() {
        questionList.getItems().clear();
        List<Question> questions = questionDAO.getQuestionsByQuizName(currentQuiz.getQuizName());
        questionList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Question question, boolean empty) {
                super.updateItem(question, empty);
                if (empty || question == null) {
                    setText(null);
                } else {
                    setText(question.getQuestionText());
                }
            }
        });
        questionList.getItems().addAll(questions);  // Geen extra DAO-call hier
    } // loadQuestionList

    // Toont waarschuwing of bevestiging tijdelijk onderin
    private void showWarning(String message) {
        warningField.setText(message);
        warningField.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> warningField.setVisible(false));
        pause.play();
    } // showWarning

} // end ManageQuestionsController
