package controller;

import database.mysql.QuestionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Group;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuestionsController {
    private QuestionDAO questionDAO;
    private User loggedInUser;
    ListView<Quiz> quizListView;

    @FXML
    ListView<Question> questionList;

    public void setup(User user) {
        loggedInUser = user;
        questionDAO = new QuestionDAO(Main.getDBAccess());
        loadQuestionList();
    }

    public void doMenu(ActionEvent actionEvent) {
//        Main.getSceneManager().showWelcomeScene(loggedInUser);
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

    public void doCreateQuestion(){
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

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

    public void doDeleteQuestion(){

    }

//    Als ik een vraag selecteer (scherm manageQuestions.fxml) wil ik meteen kunnen zien
//    hoeveel vragen er in de quiz zitten, waar de geselecteerde vraag toe behoort.

//    public void handleQuestionInfo() {
//        Question selectedQ = questionList.getSelectionModel().getSelectedItem();
//        String selectedQuizName = selectedQ.getQuiz().getQuizName();
//        if (selectedQ != null) {
//
//
//        }
//    } // handleQuestionInfo

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
} // end ManageQuestionsController
