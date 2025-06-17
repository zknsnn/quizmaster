package database.mysql;

import model.Course;
import model.Quiz;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizDAO extends AbstractDAO {
    public QuizDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    public void saveQuiz(Quiz quiz) {
        String sql = "INSERT INTO Quiz (quizName, quizLevel, succesDefinition, courseName) VALUES(?, ?, ?, ?);";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, quiz.getQuizName());
            preparedStatement.setString(2, quiz.getQuizLevel());
            preparedStatement.setDouble(3, quiz.getSuccesDefinition());
            preparedStatement.setString(4, quiz.getCourse().getCourseName());
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // einde saveQuiz

    public Quiz getQuizPerID(String quizName) {
        String sql = "SELECT * FROM Quiz WHERE quizName = ?;";
        Quiz quiz = null;
        Course course;
        // Ik heb hier courseDAO nodig.
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, quizName);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String quizLevel = resultSet.getString("quizLevel");
                double succesDefinition = resultSet.getDouble("succesDefinition");
//                course = courseDAO.getCoursePerID(courseName);
//                quiz = new Quiz(quizName, quizLevel, succesDefinition, course);
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return quiz;
    } // einde getQuiz

    public void updateQuiz() {
        // nog te maken.
        // Als input: "String quizname, String newQuizLevel, int newSuccesDefinition, Course newCourse"?
    } // einde updateQuiz

    public void deleteQuiz(String quizName) {
        String sql = "DELETE FROM Quiz WHERE quizName = '?', VALUES(?);";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, quizName);
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // einde deleteQuiz
}