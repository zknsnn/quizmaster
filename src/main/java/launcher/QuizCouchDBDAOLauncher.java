package launcher;


import com.google.gson.Gson;
import database.couchDB.CouchDBAccess;
import database.couchDB.QuizCouchDBDAO;
import model.Course;
import model.Quiz;
import model.User;
import model.UserRole;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizCouchDBDAOLauncher {

    private static CouchDBAccess couchDBAccess;
    private static QuizCouchDBDAO quizCouchDBDAO;

    public static void main(String[] args) {
        couchDBAccess = new CouchDBAccess("quizzen","admin", "admin");
        quizCouchDBDAO = new QuizCouchDBDAO(couchDBAccess);
//        saveQuizList(buildQuizList());

        User user = new User("Martin", "Wachtwoord", "Geheim", null , "Craats", UserRole.COÖRDINATOR);
        Course course = new Course("Cavia's Knippen", "Beginner", user);
        Quiz quiz = new Quiz("Awesomequiz5", "Beginner", 70, course);
//        quizCouchDBDAO.saveSingleQuiz(quiz);
        quizCouchDBDAO.deleteQuiz(quiz);
    }

    private static List<Quiz> buildQuizList() {
//		Lees de Json-string in het tekstbestand, maak er Verbruiker objecten van en sla die op in CouchDB
        Gson gson = new Gson();
        List<Quiz> quizListList = new ArrayList<>();
        try (Scanner bestandLezer = new Scanner(new File("src/main/resources/CSV bestanden/Quizzen.csv"))) {
            while (bestandLezer.hasNext()) {
                quizListList.add(gson.fromJson(bestandLezer.nextLine(), Quiz.class));
            }
        } catch (FileNotFoundException schrijfFout) {
            System.out.println("Het bestand lezen is mislukt.");
        }
        return quizListList;
    }

    private static void saveQuizList(List<Quiz> verbruikerList) {
//		Maak een connectie met CouchDB, gebruik het CouchDBaccess object.
// 		getClient() roept couchDBaccess.openConnection() aan; mocht de connection niet lukken dan is client == null;
        if	(couchDBAccess.getClient() != null) {
            System.out.println("Connection open");
//		Sla alle Verbruikers op in de CouchDb database.
            for (Quiz quiz : verbruikerList) {
                quizCouchDBDAO.saveSingleQuiz(quiz);
            }
        }
    }
}
