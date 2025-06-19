package controller;

import database.mysql.QuestionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Question;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuestionsController {
    private QuestionDAO questionDAO;
    private User user;

    @FXML
    ListView<Question> questionList;

    public void setup(User user) {
        this.user = user;
        this.questionDAO = new QuestionDAO(Main.getDBAccess());
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
    }

    public void doMenu(ActionEvent actionEvent) {
        Main.getSceneManager().showWelcomeScene(user);
    }

    public void doCreateQuestion(){
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

    public void doUpdateQuestion(){
            Main.getSceneManager().showCreateUpdateQuestionScene(questionList.getSelectionModel().getSelectedItem());
    }

    public void doDeleteQuestion(){}
}
