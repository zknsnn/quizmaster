package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;
import java.util.Optional;

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

    public void doDeleteQuiz() {
        Quiz selectedItem = quizzenLijst.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Verwijderen onsuccesvol");
            alert.setContentText("Selecteer een quiz om te verwijderen.");
            alert.show();
            return;
        }

        List<Question> vragenLijst = questionDAO.getQuestionsByQuizName(selectedItem.getQuizName());
        if (!vragenLijst.isEmpty()) {
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Verwijderen onsuccesvol");
            alert.setContentText("Het is niet mogelijk een quiz met vragen te verwijderen.");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bevestiging");
            alert.setHeaderText("Wil je de quiz '" + selectedItem.getQuizName() + "' verwijderen?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                quizDAO.deleteQuiz(selectedItem.getQuizName());
                this.alert = new Alert(Alert.AlertType.INFORMATION);
                this.alert.setTitle("Bevestiging");
                this.alert.setHeaderText(null);
                this.alert.setContentText("Quiz " + selectedItem.getQuizName() + " is verwijderd.");
                this.alert.show();
                loadQuizList();
            }
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

        if (quizzen.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Geen toegang");
            alert.setHeaderText(null);
            alert.setContentText("U bent geen coördinator van een van de beschikbare cursussen.");
            alert.show();
            Main.getSceneManager().showWelcomeScene(loggedInUser);
        }
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