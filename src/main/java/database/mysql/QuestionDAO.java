package database.mysql;

import model.Question;

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
        Question question = null;
        QuizDAO quizDAO = new QuizDAO(dbAccess);
        try {
            String sql = "SELECT * FROM Question";
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            if (resultSet.next()) {
                int questionId = resultSet.getInt("questionId");
                String questionText = resultSet.getString("questionText");
                String correctAnswer = resultSet.getString("correctAnswer");
                String wrongAnswer1 = resultSet.getString("wrongAnswer1");
                String wrongAnswer2 = resultSet.getString("wrongAnswer2");
                String wrongAnswer3 = resultSet.getString("wrongAnswer3");
                String quizName = resultSet.getString("quizName");
                question = new Question(questionId, questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quizDAO.getQuizPerID(quizName));
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
            preparedStatement.setString(6, String.valueOf(question.getQuizName()));
            preparedStatement.executeUpdate();
        } catch (SQLException sqlError) {
            System.out.println("SQL error " + sqlError.getMessage());
        }
    } // storeOne
}
