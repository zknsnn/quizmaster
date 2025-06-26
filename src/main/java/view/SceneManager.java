package view;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class SceneManager {

    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Laadt een scene
    public FXMLLoader getScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showLoginScene() {
        getScene("/view/fxml/login.fxml");
    }

    public void showWelcomeScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/welcomeScene.fxml");
        WelcomeController controller = loader.getController();
        controller.setup(user);
    }

    public void showManageUsersScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/manageUsers.fxml");
        ManageUsersController controller = loader.getController();
        controller.setup(user);
    }

    public void showCreateUpdateUserScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/createUpdateUser.fxml");
        CreateUpdateUserController controller = loader.getController();
        controller.setup(user);
    }

    public void showManageCoursesScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/manageCourses.fxml");
        ManageCoursesController controller = loader.getController();
        controller.setup(user);
    }

    public void showCreateUpdateCourseScene(Course course,User user) {
        FXMLLoader loader = getScene("/view/fxml/createUpdateCourse.fxml");
        CreateUpdateCourseController controller = loader.getController();
        controller .setup(course,user);
    }

    public void showManageGroupsScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/manageGroups.fxml");
        ManageGroupsController controller = loader.getController();
        controller.setup(user);
    }

    public void showCreateUpdateGroupScene(Group group, User currentUser) {
        FXMLLoader loader = getScene("/view/fxml/createUpdateGroup.fxml");
        CreateUpdateGroupController controller = loader.getController();
        controller.setup(group, currentUser);
    }

    public void showManageQuizScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/manageQuizzes.fxml");
        ManageQuizzesController controller = loader.getController();
        controller.setup(user);
    }

    public void showCreateUpdateQuizScene(Quiz quiz, User user) {
//        System.out.println("***** showCreateUpdateQuizScene" + user + quiz);
        FXMLLoader loader = getScene("/view/fxml/createUpdateQuiz.fxml");
        CreateUpdateQuizController controller = loader.getController();
        controller.setup(quiz, user);
    }

    public void showManageQuestionsScene(Quiz quiz, User user) {
        FXMLLoader loader = getScene("/view/fxml/manageQuestions.fxml");
        ManageQuestionsController controller = loader.getController();
        controller.setup(quiz, user);
    }

    public void showCreateUpdateQuestionScene(Question question, Quiz quiz) {
        FXMLLoader loader = getScene("/view/fxml/createUpdateQuestion.fxml");
        CreateUpdateQuestionController controller = loader.getController();
        controller.setup(question, quiz);
    }

    public void showStudentSignInOutScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/studentSignInOut.fxml");
        StudentSignInOutController controller = loader.getController();
        controller.setup(user);
    }

    public void showSelectQuizForStudent(User user) {
        FXMLLoader loader = getScene("/view/fxml/selectQuizForStudent.fxml");
        SelectQuizForStudentController controller = loader.getController();
        controller.setup(user);
    }

    public void showFillOutQuiz(Quiz quiz, User user) {
        FXMLLoader loader = getScene("/view/fxml/fillOutQuiz.fxml");
        FillOutQuizController controller = loader.getController();
        controller.setup(user, quiz);
    }

    public void showStudentFeedback(User user, Quiz quiz) {
        FXMLLoader loader = getScene("/view/fxml/studentFeedback.fxml");
        StudentFeedbackController controller = loader.getController();
        controller.setup(user, quiz);
    }

    public void showCoordinatorDashboard(User user) {
        FXMLLoader loader = getScene("/view/fxml/coordinatorDashboard.fxml");
        CoordinatorDashboardController controller = loader.getController();
        controller.setup(user);
    }

    public void showAssignStudentsToGroupScene(User user) {
        FXMLLoader loader = getScene("/view/fxml/assignStudentsToGroup.fxml");
        AssignStudentsToGroupController controller = loader.getController();
        controller.setup(user);
    }
}
