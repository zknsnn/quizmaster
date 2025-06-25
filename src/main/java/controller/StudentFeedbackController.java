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

    @FXML
    private Label feedbackLabel;
    @FXML
    private ListView<QuizResult> feedbackList;

    public void setup(User user, Quiz quiz) {
        this.user = user;
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(user);
    }
}

