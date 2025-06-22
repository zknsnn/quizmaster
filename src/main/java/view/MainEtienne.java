package view;

import database.mysql.DBAccess;
import database.mysql.UserDAO;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Course;
import model.User;

public class MainEtienne extends Main {



    public static void main(String[] args) {
        launch(args);
    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        super.start( primaryStage );
//        primaryStage.setTitle("Make IT Work - Project 1");
//
//        getSceneManager().showCreateUpdateUserScene(null);
//        primaryStage.show();
//        }

    @Override
    public void start(Stage primaryStage) {
        super.start( primaryStage );
        primaryStage.setTitle("Make IT Work - Project 1");

        getSceneManager().showManageUsersScene(null);
        primaryStage.show();
    }

}
