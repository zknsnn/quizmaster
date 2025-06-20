package controller;

import model.Question;
import model.User;
import view.Main;

public class CreateUpdateQuestionController {
    private User user;

    public void setup(Question question) {}

    public void doMenu() {
        Main.getSceneManager().showManageQuestionsScene(user);
    }

    public void doCreateUpdateQuestion() {
//
//        if(currentquestion != null){
//            vulalleveldenmetdeinhoud
//        }

    }
} // CreateUpdateQuestionController