package controller;

import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuestionsController {
    private QuestionDAO questionDAO;
    private QuizDAO quizDAO;
    private User loggedInUser;

    @FXML
    private ListView<Question> questionList;
    @FXML
    private Label quizQuestionsCountLabel;
    @FXML
    private ListView<Quiz> quizListView;
    List<Question> questions;
    List<Quiz> quizzes;

    public void setup(User user) {
        this.loggedInUser = user;
        this.questionDAO = new QuestionDAO(Main.getDBAccess());
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        this.questions = questionDAO.getAll();      // Eenmalig ophalen
        this.quizzes = quizDAO.getAllQuizzes();     // Eenmalig ophalen
        loadQuestionList();
    }

    @FXML
    public void doMenu(ActionEvent actionEvent) {
//        Main.getSceneManager().showWelcomeScene(loggedInUser);
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

    @FXML
    public void doCreateQuestion(){
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

    @FXML
    public void doUpdateQuestion(){
//            Main.getSceneManager().showCreateUpdateQuestionScene(questionList.getSelectionModel().getSelectedItem());
            Question selectedQuestion = questionList.getSelectionModel().getSelectedItem();
            if(selectedQuestion == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Let op !!");
                alert.setHeaderText("Selecteer een vraag.");
                alert.showAndWait();
            } else {
                Main.getSceneManager().showCreateUpdateQuestionScene(selectedQuestion);
            }
    }

    @FXML
    public void doDeleteQuestion(){
        Question selectedQuestion = questionList.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Let op!");
            alert.setHeaderText("Selecteer eerst een vraag om te verwijderen.");
            alert.showAndWait();
            return;
        }
        questionDAO.deleteQuestion(selectedQuestion);
        loadQuestionList();

    }

//    Als ik een vraag selecteer (scherm manageQuestions.fxml) wil ik meteen kunnen zien
//    hoeveel vragen er in de quiz zitten, waar de geselecteerde vraag toe behoort.

    public void handleQuestionInfo() {
        List<Question> questions = questionDAO.getAll();
        questionList.getItems().addAll(questions);
        Question selectedQ = questionList.getSelectionModel().getSelectedItem();
        if (selectedQ != null && selectedQ.getQuiz() != null) {
            String selectedQuizName = selectedQ.getQuiz().getQuizName();
//            long quizCount = quizListView.setCellFactory(ListView<Quiz> )
            long quizCount = questions.stream()
                    .filter(quiz -> selectedQuizName.equals(quiz.getQuiz().getQuizName())).count();
            quizQuestionsCountLabel.setText("Aantal vragen in quiz \"" + selectedQuizName + "\" : " + quizCount);
        } else {
            quizQuestionsCountLabel.setText("Geen quiz gekoppeld aan deze vraag.");
        }
    } // handleQuestionInfo

// Aamaken van de lijsten duurt te lang in oude code
/*
    private void loadQuestionList() {
        questionList.getItems().clear();
//        Rendering van elke vraag in de ListView
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
        List<Question> questions = questionDAO.getAll();
        questionList.getItems().addAll(questions);
    } // end loadQuestionList
 */

// Omdat het creeren van de lijsten heel traag ging, ChatGPT om vereenvoudiging gevraagd.
// Ook dit werkt nog niet echt.
    private void loadQuestionList() {
        questionList.getItems().clear();
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

} // end ManageQuestionsController
