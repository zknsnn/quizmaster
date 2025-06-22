package controller;

import database.mysql.QuizDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Quiz;
import model.User;
import view.Main;

import java.util.List;

public class ManageQuizzesController {
    private QuizDAO quizDAO;
    private User loggedInUser;

    @FXML
    ListView<Quiz> quizzenLijst;

    public void setup(User user) {
        this.loggedInUser = user;
        this.quizDAO = new QuizDAO(Main.getDBAccess());
        loadQuizList();
    }

    public void doMenu(ActionEvent actionEvent){
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }

    public void doCreateQuiz(){
        Main.getSceneManager().showCreateUpdateQuizScene(null, loggedInUser);
    }

    public void doUpdateQuiz(){
        Quiz quiz = quizzenLijst.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wijzigen");
            alert.setHeaderText("Geen quiz geselecteerd");
            alert.setContentText("Selecteer een quiz om te wijzigen.");
            alert.showAndWait();
        } else {
            Main.getSceneManager().showCreateUpdateQuizScene(quiz, loggedInUser);
        }
    } // einde doUpdateQuiz

    public void doDeleteQuiz(){
        Quiz selectedItem = quizzenLijst.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Verwijderen onsuccesvol");
            alert.setContentText("Selecteer een quiz om te verwijderen.");
            alert.showAndWait();
        } else {
            quizDAO.deleteQuiz(selectedItem.getQuizName());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Verwijderen");
            alert.setHeaderText("Quiz verwijderd");
            alert.setContentText("Quiz " + selectedItem.getQuizName() + " is verwijderd.");
            alert.showAndWait();
            loadQuizList();
        }
    } // einde doDeleteQuiz

    public void loadQuizList() {
        quizzenLijst.getItems().clear();
        List<Quiz> quizzen = quizDAO.getAllQuizzes();
        quizzenLijst.getItems().addAll(quizzen);
    }
}