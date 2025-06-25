package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Quiz;
import model.QuizResult;
import model.User;
import view.Main;

public class StudentFeedbackController {
    User user;
    Quiz quiz;

    @FXML
    private Label feedbackLabel;
    @FXML
    private ListView<QuizResult> feedbackList;

//    Quiz afsluiten en feedback Als student wil ik na de beantwoording van de laatste vraag een
//    scherm zien met daarop een overzicht van alle keren dat ik deze quiz heb gedaan met
//    daarbij het behaalde resultaat (scherm studentFeedback.fxml).


    public void setup(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(user);
    }
}

