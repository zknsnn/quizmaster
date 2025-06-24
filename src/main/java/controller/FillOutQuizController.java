package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Quiz;
import model.User;
import view.Main;

public class FillOutQuizController {
    User loggedInUser;

    @FXML
    private Label titleLabel;
    @FXML
    private TextArea questionArea;

    public void setup(User user, Quiz quiz) {
        this.loggedInUser = user;
    }

    public void doRegisterA() {}

    public void doRegisterB() {}

    public void doRegisterC() {}

    public void doRegisterD() {}

    public void doNextQuestion() {}

    public void doPreviousQuestion() {}

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }
}
