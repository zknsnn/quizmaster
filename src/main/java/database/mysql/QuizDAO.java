package database.mysql;

import model.Course;
import model.Inschrijving;
import model.Quiz;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO extends AbstractDAO {
    private CourseDAO courseDAO;

    public QuizDAO(DBAccess dbAccess) {
        super(dbAccess);
        courseDAO = new CourseDAO(dbAccess);
    }

    public void saveQuiz(Quiz quiz) {
        String sql = "INSERT INTO Quiz(quizName, quizLevel, succesDefinition, courseName) VALUES(?, ?, ?, ?);";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, quiz.getQuizName());
            preparedStatement.setString(2, quiz.getQuizLevel());
            preparedStatement.setDouble(3, quiz.getSuccesDefinition());
            preparedStatement.setString(4, quiz.getCourse().getCourseName());
            executeManipulateStatement();
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // einde saveQuiz

    public Quiz getQuizPerID(String quizName) {
        String sql = "SELECT * FROM Quiz WHERE quizName = ?;";
        Quiz quiz = null;
        Course course;
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, quizName);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String quizLevel = resultSet.getString("quizLevel");
                double succesDefinition = resultSet.getDouble("succesDefinition");
                String courseNaam = resultSet.getString("courseName");
                course = courseDAO.getOneByName(courseNaam);
                quiz = new Quiz(quizName, quizLevel, succesDefinition, course);
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return quiz;
    } // einde getQuiz

    public void updateQuiz(Quiz quiz) {
        String sql = "UPDATE Quiz SET quizLevel = ?, succesDefinition = ?, courseName = ? WHERE quizName = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, quiz.getQuizLevel());
            preparedStatement.setString(2, String.valueOf(quiz.getSuccesDefinition()));
            preparedStatement.setString(3, quiz.getCourse().getCourseName());
            preparedStatement.setString(4, quiz.getQuizName());
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij updaten van cursus: " + e.getMessage());
        }
    } // einde updateQuiz

    public void deleteQuiz(String quizName) {
        String sql = "DELETE FROM Quiz WHERE quizName = ?;";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, quizName);
            executeManipulateStatement();
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // einde deleteQuiz

    public ArrayList<Quiz> getQuizzesPerCoordinator(User user) {
        ArrayList<Quiz> arrayListQuizzes = new ArrayList<>();
        String sql = "SELECT * FROM Quiz JOIN Course ON Quiz.courseName = Course.courseName WHERE coordinator = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, user.getUserName());
            ResultSet resultSet = executeSelectStatement();
            Quiz quiz;
            Course course;
            while (resultSet.next()) {
                String quizName = resultSet.getString("quizName");
                String quizLevel = resultSet.getString("quizLevel");
                double succesDefinition = resultSet.getDouble("succesDefinition");
                String courseNaam = resultSet.getString("courseName");
                course = courseDAO.getOneByName(courseNaam);
                quiz = new Quiz(quizName, quizLevel, succesDefinition, course);
                arrayListQuizzes.add(quiz);
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return arrayListQuizzes;
    }

    public ArrayList<Quiz> getAllQuizzes() {
        String sql = "SELECT * FROM Quiz";
        ArrayList<Quiz> arrayListQuizzes = new ArrayList<>();
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            Quiz quiz;
            Course course;
            while (resultSet.next()) {
                String quizName = resultSet.getString("quizName");
                String quizLevel = resultSet.getString("quizLevel");
                double succesDefinition = resultSet.getDouble("succesDefinition");
                String courseNaam = resultSet.getString("courseName");
                course = courseDAO.getOneByName(courseNaam);
                quiz = new Quiz(quizName, quizLevel, succesDefinition, course);
                arrayListQuizzes.add(quiz);
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL error " + sqlException.getMessage());
        }
        return arrayListQuizzes;
    } // einde getAllQuizes

    public ArrayList<Quiz> getQuizPerCourseName(String courseName) {
        String sql = "SELECT * FROM Quiz WHERE courseName = ?;";
        ArrayList<Quiz> quizList = new ArrayList<>();
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = executeSelectStatement();
            Course course = courseDAO.getOneByName(courseName);
            while (resultSet.next()) {
                String quizName = resultSet.getString("quizName");
                String quizLevel = resultSet.getString("quizLevel");
                double succesDefinition = resultSet.getDouble("succesDefinition");

                Quiz quiz = new Quiz(quizName, quizLevel, succesDefinition, course);
                quizList.add(quiz);
            }
        } catch (SQLException e) {
            System.out.println("SQL error " + e.getMessage());
        }
        return quizList;
    }

    public List<Quiz> getQuizPerStudent(User user) {
        InschrijvingDAO inschrijvingDAO = new InschrijvingDAO(dbAccess);
        // hier haal ik de inschrijvingen in een course op per student
        List<Inschrijving> inschrijvingList = inschrijvingDAO.getInschrijvingByStudentname(user.getUserName());

        List<Quiz> quizListPerStudent = new ArrayList<>();
        for (Inschrijving inschrijving : inschrijvingList) {
            String courseName = inschrijving.getCourse().getCourseName();
            quizListPerStudent.addAll(getQuizPerCourseName(courseName));
        }
        return quizListPerStudent;
    } // einde getQuizPerStudent
}