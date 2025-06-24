package controller;

import database.mysql.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import model.Course;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorDashboardController {

    @FXML
    private ListView<Course> courseList;
    @FXML
    private ListView<Quiz> quizList;
    @FXML
    private ListView<Integer> questionList;

    private User ingelogdeuser;
    private CourseDAO courseDAO;
    private QuizDAO quizDAO;
    private QuestionDAO questionDAO;


    public void setup(User user) {
        this.ingelogdeuser = user;
        this.courseDAO = new CourseDAO(Main.getDBAccess());
        this.quizDAO = new QuizDAO(Main.getDBAccess());   // burayı eklemeyi unutma
        this.questionDAO = new QuestionDAO(Main.getDBAccess());

        // Wanneer een cursus wordt gekozen, laad de quizzen van die cursus
        courseList.getSelectionModel().selectedItemProperty().addListener((observable, oldCourse, newCourse) -> {
            quizList.getItems().clear();
            if (newCourse != null) {
                List<Quiz> quizzes = quizDAO.getQuizPerCourseName(newCourse.getCourseName());
                quizList.getItems().addAll(quizzes);
            }
        });

        // Wanneer een quiz wordt gekozen, laad de vragen van die quiz
        quizList.getSelectionModel().selectedItemProperty().addListener((obs, oldQuiz, newQuiz) -> {
            questionList.getItems().clear();
            if (newQuiz != null) {
                List<Question> questions = questionDAO.getQuestionsByQuizName(newQuiz.getQuizName());
                for (Question q : questions) {
                    questionList.getItems().add(q.getQuestionId());
                }
            }
        });


        List<Course> courses = courseDAO.getAll();
        courseList.getItems().addAll(courses);
    }


    public void doNewQuiz() {
        Main.getSceneManager().showCreateUpdateQuizScene(null,ingelogdeuser);
    }

    public void doEditQuiz() {
        Quiz selectedQuiz = quizList.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij updaten");
            alert.setHeaderText("Update is mislukt");
            alert.setContentText("Selecteer een quiz om te updaten.");
            alert.showAndWait();
        }else {
            Main.getSceneManager().showCreateUpdateQuizScene(selectedQuiz,ingelogdeuser);
        }

    }

    public void doNewQuestion() {
        Main.getSceneManager().showCreateUpdateQuestionScene(null);
    }

    public void doEditQuestion() {
        Integer idQuestion = questionList.getSelectionModel().getSelectedItem();
        Question selectedQuestion = questionDAO.getOneById(idQuestion);

        if (selectedQuestion == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout bij updaten");
            alert.setHeaderText("Update is mislukt");
            alert.setContentText("Selecteer een question om te updaten.");
            alert.showAndWait();
        }else {
            Main.getSceneManager().showCreateUpdateQuestionScene(selectedQuestion);
        }
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(Main.currentUser());
    }
}
