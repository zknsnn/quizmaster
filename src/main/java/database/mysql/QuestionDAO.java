package database.mysql;

import model.Question;
import model.Quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QuestionDAO extends AbstractDAO implements GenericDAO<Question> {

    public QuestionDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    public List<Question> getAll() {
        List<Question> questionList = new ArrayList<>();
        Question question;
        QuizDAO quizDAO = new QuizDAO(dbAccess);
        try {
            String sql = "SELECT * FROM Question";
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                int questionId = resultSet.getInt("questionId");
                String questionText = resultSet.getString("questionText");
                String correctAnswer = resultSet.getString("correctAnswer");
                String wrongAnswer1 = resultSet.getString("wrongAnswer1");
                String wrongAnswer2 = resultSet.getString("wrongAnswer2");
                String wrongAnswer3 = resultSet.getString("wrongAnswer3");
                String quizname = resultSet.getString("quizName");
                question = new Question(questionId, questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quizDAO.getQuizPerID(quizname));
                questionList.add(question);
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return questionList;
    } // getAll

    @Override
    public Question getOneById(int id) {
        String sql = "SELECT * FROM Question WHERE questionId = ?";
        Question question = null;
        QuizDAO quizDAO = new QuizDAO(dbAccess);
        try {
            setupPreparedStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = executeSelectStatement();
            if (resultSet.next()) {
                Quiz quiz = quizDAO.getQuizPerID(resultSet.getString("quizName"));

                question = new Question(
                        resultSet.getString("questionText"),
                        resultSet.getString("correctAnswer"),
                        resultSet.getString("wrongAnswer1"),
                        resultSet.getString("wrongAnswer2"),
                        resultSet.getString("wrongAnswer3"),
                        quiz
                );
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return question;
    } // getOneById

    @Override
    public void storeOne(Question question) {
        String sql = "INSERT INTO Question (questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quizName) VALUES(?, ?, ?, ?, ?, ?);";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getCorrectAnswer());
            preparedStatement.setString(3, question.getWrongAnswer1());
            preparedStatement.setString(4, question.getWrongAnswer2());
            preparedStatement.setString(5, question.getWrongAnswer3());
            preparedStatement.setString(6, question.getQuiz().getQuizName());
            executeManipulateStatement();
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // storeOne

    public void updateQuestion(Question question) {
        String sql = "UPDATE Question SET questionText = ?, correctAnswer = ?, wrongAnswer1 = ?, wrongAnswer2 = ?, wrongAnswer3 = ?, quizName = ? WHERE questionId = ?;";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getCorrectAnswer());
            preparedStatement.setString(3, question.getWrongAnswer1());
            preparedStatement.setString(4, question.getWrongAnswer2());
            preparedStatement.setString(5, question.getWrongAnswer3());
            preparedStatement.setString(6, question.getQuiz().getQuizName());
            preparedStatement.setInt(7, question.getQuestionId());
            executeManipulateStatement();
            System.out.println("Question updated successfully: " + question.getQuestionText());
        } catch (SQLException e) {
            System.out.println("Fout bij het updaten van vraag: " + e.getMessage());
        }
    }

    public void deleteQuestion(Question question) {
        String sql = "DELETE FROM Question WHERE questionId = ?;";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setInt(1, question.getQuestionId());
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij het verwijderen van vraag: " + e.getMessage());
        }
    } // deleteQuestion

    public List<Question> getQuestionsByQuizName(String quizName) {
        List<Question> questionList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Question WHERE quizName = ?";
            setupPreparedStatement(sql);
            preparedStatement.setString(1, quizName);
            ResultSet resultSet = executeSelectStatement();
            QuizDAO quizDAO = new QuizDAO(dbAccess);
            while (resultSet.next()) {
                int questionId = resultSet.getInt("questionId");
                String questionText = resultSet.getString("questionText");
                String correctAnswer = resultSet.getString("correctAnswer");
                String wrongAnswer1 = resultSet.getString("wrongAnswer1");
                String wrongAnswer2 = resultSet.getString("wrongAnswer2");
                String wrongAnswer3 = resultSet.getString("wrongAnswer3");
                Question question = new Question(
                        questionId, questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3,
                        quizDAO.getQuizPerID(quizName)
                );
                questionList.add(question);
            }
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
        return questionList;
    }
}
