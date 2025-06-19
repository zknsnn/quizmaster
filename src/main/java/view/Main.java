package view;

import database.mysql.DBAccess;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static DBAccess dbAccess;
    private static SceneManager sceneManager = null;
    private static Stage primaryStage = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Make IT Work - Project 1");
        getSceneManager().showManageUserScene(null);
        primaryStage.show();
    }

    public static DBAccess getDBAccess() {
        if (dbAccess == null) {
            dbAccess = new DBAccess("zgrootrc2", "grootrc2", "wCO5RDWS3Br/es7X");
            dbAccess.openConnection();
        }
        return dbAccess; 
    }

    public static SceneManager getSceneManager() {
        if (sceneManager == null) {
            sceneManager = new SceneManager(primaryStage);
        }
        return sceneManager;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}