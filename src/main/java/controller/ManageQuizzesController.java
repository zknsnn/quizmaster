package controller;

import database.mysql.DBAccess;
import database.mysql.QuizDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Quiz;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class ManageQuizzesController {
    private QuizDAO quizDAO;
    private User user;

    @FXML
    ListView<Quiz> quizzenLijst;

//    @FXML
//    TextField waarschuwingTextField;

    // Ik wil eerst alle quizen inladen in mijn quizzenlijst
    // Je moet een quiz kunnen aanklikken.
    // Bij "Nieuw" ga je een quiz aanmaken
    // Bij "Wijzig" ga je een bestaande quiz wijzigen
    // Bij "Verwijder" verwijder je de aangeklikte quiz
    // Bij "Menu" ga je weer terug naar je vandaan kwam.

    public void setup(User user) {
        this.user = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        quizzenLijst.getItems().clear();
        List<Quiz> quizzen = quizDAO.getAllQuizzes();
        System.out.println(quizzen);
        quizzenLijst.getItems().addAll(quizzen);
    }

    public void doMenu(ActionEvent actionEvent){
        Main.getSceneManager().showWelcomeScene(user);
    }

    public void doCreateQuiz(){
        Main.getSceneManager().showCreateUpdateQuizScene(null);
    }

    public void doUpdateQuiz(){
        Main.getSceneManager().showCreateUpdateQuizScene(null);
    }

    public void doDeleteQuiz(){}
}