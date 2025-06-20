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
    private User loggedInUser;

    @FXML
    ListView<Quiz> quizzenLijst;

//    @FXML
//    TextField waarschuwingTextField;

    // Ik wil eerst alle quizen inladen in mijn quizzenlijst - done
    // Je moet een quiz kunnen aanklikken.
    // Bij "Nieuw" ga je een quiz aanmaken
    // Bij "Wijzig" ga je een bestaande quiz wijzigen
    // Bij "Verwijder" verwijder je de aangeklikte quiz
    // Bij "Menu" ga je weer terug naar je vandaan kwam.

    public void setup(User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        quizzenLijst.getItems().clear();
        List<Quiz> quizzen = quizDAO.getAllQuizzes();
        quizzenLijst.getItems().addAll(quizzen);

        // Quiznaam, Quizlevel, Course
        // Tabelvorm (als dat lukt) anders fixen met uitlijnen.
        // Volgorde moet alfabetisch
        // Anderen vragen welke informatie ze moeten tonen.
    }

    public void doMenu(ActionEvent actionEvent){
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doCreateQuiz(){
        Main.getSceneManager().showCreateUpdateQuizScene(null, loggedInUser);
    }

    public void doUpdateQuiz(){
        Quiz quiz = quizzenLijst.getSelectionModel().getSelectedItem();
//        if (quiz == null) {
//            quizzenLijst.getSelectionModel().selectFirst();
//        }
        System.out.println("uit quizzenlijst" +quiz);
        System.out.println("print user" + loggedInUser);
        Main.getSceneManager().showCreateUpdateQuizScene(quiz, loggedInUser);

//        Main.getSceneManager().showCreateUpdateQuizScene(null);
    }

    public void doDeleteQuiz(){}
}