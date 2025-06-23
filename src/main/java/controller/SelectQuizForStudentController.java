package controller;

import database.mysql.QuizDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;


// Quiz selecteren Als student wil ik een quiz kunnen selecteren zodat ik die vervolgens kan gaan invullen.
// Op het selectiescherm worden alleen de quizzen aangeboden van de cursussen waarvoor ik ben ingeschreven.
// Een student mag een quiz meerdere keren invullen. Bij een quiz die de student al eerder heeft ingevuld wordt
// achter de naam van de quiz de datum van de laatste poging en het resultaat daarvan getoond (scherm
// selectQuizForStudent.fxml).

// 1. Haal alle quizzen op die bij een course horen waar ik voor ben ingeschreven

public class SelectQuizForStudentController {
    private QuizDAO quizDAO;
    private User loggedInUser;


    @FXML
    ListView<Quiz> quizList;

    public void setup(User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());

        quizList.getItems().clear();
        List<Quiz> quizzen = quizDAO.getQuizPerStudent(user);
        System.out.println(quizzen);
        quizList.getItems().addAll(quizzen);
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doQuiz() {
        Main.getSceneManager().showManageQuizScene(loggedInUser);
    }
}
