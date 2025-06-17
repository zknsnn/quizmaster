package controller;

import model.User;
import view.Main;

public class ManageCoursesController {

    private User user;

    public void setup(User user) {
        this.user = user;
    }

    public void doMenu(){
        Main.getSceneManager().showWelcomeScene(user);
    }

    public void doCreateCourse(){}

    public void doUpdateCourse(){}

    public void doDeleteCourse(){}

}
