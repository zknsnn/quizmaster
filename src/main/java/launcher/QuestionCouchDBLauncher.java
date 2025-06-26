package launcher;

import com.google.gson.Gson;
import database.couchDB.CouchDBAccess;
import database.couchDB.QuestionCouchDBDAO;
import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class QuestionCouchDBLauncher {
    private static CouchDBAccess couchDBAccess;
    private static QuestionCouchDBDAO questionCouchDBDAO;

    DBAccess dbAccess = Main.getDBAccess();
    QuestionDAO questionDAO = new QuestionDAO(dbAccess);
    QuizDAO quizDAO = new QuizDAO(dbAccess);
    private ListView<Question> questionList;
    private static Question question;

    public static void main(String[] args) {
        DBAccess dbAccess = Main.getDBAccess();
        QuestionDAO questionDAO = new QuestionDAO(dbAccess);
        QuizDAO quizDAO = new QuizDAO(dbAccess);

        couchDBAccess = new CouchDBAccess("questions2", "admin", "admin");
        questionCouchDBDAO = new QuestionCouchDBDAO(couchDBAccess);

        // Testen of de CouchDB connection open is
        if (couchDBAccess.getClient() != null) {
            System.out.println("CouchDB connection open");
        }

        List<Question> questions = questionDAO.getAll();
        for (Question question : questions) {
            questionCouchDBDAO.storeOne(question);
        }

        questions = questionCouchDBDAO.getQuestions("cavia");
        for (Question question : questions) {
            System.out.println(question.getQuestionText());
        }
    }
}
