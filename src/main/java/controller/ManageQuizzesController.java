package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuizzesController {
    private QuizDAO quizDAO;
    private User loggedInUser;
    private QuestionDAO questionDAO;
    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    ListView<Quiz> quizzenLijst;

    @FXML
    private Label quizQuestionsCountLabel;

    public void setup(User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        this.questionDAO = new QuestionDAO(Main.getDBAccess());
        loadQuizList();
    }

    public void doMenu(){
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doCreateQuiz(){
        Main.getSceneManager().showCreateUpdateQuizScene(null, loggedInUser);
    }

    public void doUpdateQuiz(){
        Quiz quiz = quizzenLijst.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            alert.setTitle("Wijzigen");
            alert.setHeaderText("Geen quiz geselecteerd");
            alert.setContentText("Selecteer een quiz om te wijzigen.");
            alert.showAndWait();
        } else {
            Main.getSceneManager().showCreateUpdateQuizScene(quiz, loggedInUser);
        }
    } // einde doUpdateQuiz

    public void doDeleteQuiz(){
        Quiz selectedItem = quizzenLijst.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Verwijderen onsuccesvol");
            alert.setContentText("Selecteer een quiz om te verwijderen.");
            alert.showAndWait();
        } else {
            quizDAO.deleteQuiz(selectedItem.getQuizName());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Quiz verwijderd");
            alert.setContentText("Quiz " + selectedItem.getQuizName() + " is verwijderd.");
            alert.showAndWait();
            loadQuizList();
        }
    } // einde doDeleteQuiz

    public void doUpdateQuestion() {
        Quiz quiz = quizzenLijst.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            alert.setTitle("Question beheer");
            alert.setHeaderText("Geen quiz geselecteerd");
            alert.setContentText("Selecteer quiz om vragen te beheren.");
            alert.showAndWait();
        } else {
            Main.getSceneManager().showManageQuestionsScene(quiz, loggedInUser);
        }
    }

        public void loadQuizList() {
        quizzenLijst.getItems().clear();
        List<Quiz> quizzen = quizDAO.getQuizzesPerCoordinator(loggedInUser);
        quizzenLijst.getItems().addAll(quizzen);
    }

    public void handleQuestionInfo() {
        List<Question> questions = questionDAO.getQuestionsByQuizName(quizzenLijst.getSelectionModel().getSelectedItem().getQuizName());
        Quiz quizz = quizzenLijst.getSelectionModel().getSelectedItem();
        if (quizz != null) {
            String selectedQuizName = quizz.getQuizName();
            long quizCount = questions.stream().filter(quiz -> selectedQuizName.equals(quiz.getQuiz().getQuizName())).count();
            quizQuestionsCountLabel.setText("Aantal vragen in quiz " + quizCount);
        } else {
            quizQuestionsCountLabel.setText("Geen vragen gekoppeld aan deze quiz.");
        }
    } // einde handleQuestionInfo
}