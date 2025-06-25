package controller;

import database.couchDB.QuizResultCouchDBDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import model.QuizResult;
import model.User;
import view.Main;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentFeedbackController {
    User user;
    Quiz quiz;
    private QuizResult quizResult;
    private List<QuizResult> lijstQuizResultaten;
    private QuizResultCouchDBDAO quizResultCouchDBDAO;

    @FXML
    private Label feedbackLabel;
    @FXML
    private ListView<QuizResult> feedbackList;

//    Quiz afsluiten en feedback Als student wil ik na de beantwoording van de laatste vraag een
//    scherm zien met daarop een overzicht van alle keren dat ik deze quiz heb gedaan met


    public void setup(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        quizResultCouchDBDAO = new QuizResultCouchDBDAO(Main.getCouchDBAccess());
        lijstQuizResultaten = quizResultCouchDBDAO.getAllQuizResults(quiz, user);
        feedbackLabel.setText("Feedback voor quiz " + quiz.getQuizName());
        loadQuizList();
    }

    private void loadQuizList() {
    feedbackList.getItems().clear();
    lijstQuizResultaten = quizResultCouchDBDAO.getAllQuizResults(quiz, user);
    lijstQuizResultaten.sort(new Comparator<QuizResult>() {
        @Override
        public int compare(QuizResult qr2, QuizResult qr1) {
            return qr1.getTijdstipQuiz().compareTo(qr2.getTijdstipQuiz());
        }

    });
    feedbackList.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(QuizResult quizResult, boolean empty) {
                    super.updateItem(quizResult, empty);
                    if (empty || quizResult == null) {
                        setText(null);
                    } else {
                        if (quizResult.getVoldoende()) {
                            setText(quizResult.getQuiz().getQuizName() + " - " + String.valueOf(quizResult
                                    .getTijdstipQuiz()) + " - " + "voldoende");
                        } else {
                            setText(quizResult.getQuiz().getQuizName() + " - " + String.valueOf(quizResult
                                    .getTijdstipQuiz()) + " - " +  "onvoldoende");
                        }
                    }
                }
    });
    feedbackList.getItems().addAll(lijstQuizResultaten);
} // loadQuizList


    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(user);
    }
}

