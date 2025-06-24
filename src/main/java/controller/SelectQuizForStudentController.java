package controller;

import database.mysql.QuizDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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


public class SelectQuizForStudentController {
    private QuizDAO quizDAO;
    private User loggedInUser;
    private Quiz quiz;
    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    ListView<Quiz> quizList;

    public void setup(User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());

        quizList.getItems().clear();
        List<Quiz> quizzen = quizDAO.getQuizPerStudent(user);
        quizList.getItems().addAll(quizzen);

        if (quizzen.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Quiz");
            alert.setHeaderText("Geen quizzen gevonden");
            alert.setContentText("Er zijn geen quizzen die je kunt maken. Controleer of je bent " +
                    "ingeschreven voor een cursus.");
            alert.show();
            Main.getSceneManager().showWelcomeScene(loggedInUser);
        }
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doQuiz() {
        Quiz quiz = quizList.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            alert.setTitle("Quiz");
            alert.setHeaderText("Geen quiz geselecteerd");
            alert.setContentText("Selecteer een quiz om te maken.");
            alert.show();
        } else {
            Main.getSceneManager().showFillOutQuiz(quiz, loggedInUser);
        }
    }
}