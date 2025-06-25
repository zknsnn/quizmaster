package launcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.CouchDBAccess;
import database.mysql.*;
import javafx.scene.control.ListView;
import model.Question;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LauncherVanRonald {
    private final QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());

    private ListView<Question> questionList;
    private static Question question;
    private static CouchDBAccess couchDBAccess;
    private static QuestionCouchDBDAO questionCouchDBDAO;

    public static final String MSG_FOUT_LEEGMAKEN_TABEL = "Fout bij leegmaken tabel: ";
    public static final String MSG_TABEL_USER_GELEEGD = "Tabel Question geleegd.";

    public static void main(String[] args) {

        DBAccess dbAccess = Main.getDBAccess();
        QuestionDAO questionDAO = new QuestionDAO(dbAccess);
        QuizDAO quizDAO = new QuizDAO(dbAccess);
        dbAccess.openConnection();

        couchDBAccess = new CouchDBAccess("questions","admin", "admin");
        questionCouchDBDAO = new QuestionCouchDBDAO(couchDBAccess);

        // Testen of de CouchDB connection open is
        if (couchDBAccess.getClient() != null) {
            System.out.println("CouchDB connection open");
        }

        // Nu nog iets van een score lijst vullen
        // iets van : questionScoreList(buildQuestionScoreList());
/*
        List<Question> questions = questionDAO.getAll();{
            Gson gson = new Gson();
            List<Question> question = new ArrayList<>();
            // Haal alle documenten op, in de vorm van JsonObjecten;
            // zet om naar Vraag om het mogelijk te maken om op deel van vraag te zoeken
            for (JsonObject jsonObject : getAllDocuments()) {
                Question result = gson.fromJson(jsonObject, Question.class);
                if (result.getQuestion().contains(question)) {
                    questions.add(result);
                }
            }
            return questions;
        }
 */
        // Haal een lijst vragen uit de SQL DB en sla op in Json
/*        List<Question> questions = questionDAO.getAll();
        private String saveSingleQuestion(questions) {
            // Vraag object omzetten naar JsonObject, zodat het opgeslagen kan worden mbv save()
            Gson gson = new Gson();
            String jsonString = gson.toJson(question);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
            return questionCouchDBDAO.saveDocument(jsonObject);
        } // saveSingleQuestion

 */



        // Eerst de database legen om te kunnen testen / demo
//        deleteFromTable(dbAccess);

        // Functionaliteit om de meegeleverde csv-bestanden in te lezen en weg te schrijven in de database.
        File questionFile = new File("src/main/resources/CSV bestanden/Vragen.csv");
//        List<Question> questionList = new ArrayList<>();
        try {
            String line;
            Scanner scanner = new Scanner(questionFile);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] lineSplit = line.split(";");
                String questionText = lineSplit[0];
                String correctAnswer = lineSplit[1];
                String wrongAnswer1 = lineSplit[2];
                String wrongAnswer2 = lineSplit[3];
                String wrongAnswer3 = lineSplit[4];
                String quizName = lineSplit[5];
                Quiz quiz = quizDAO.getQuizPerID(quizName); // QuizDAO - quizByName
                // add line by line into questionList
                // questionList.add(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz));
                questionDAO.storeOne(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz));
            }
        } catch (IOException e) {
            // Afhandeling voor IOException
            System.err.println("IOException error: " + e.getMessage());
        } catch (Exception e) {
            // Algemene afhandeling voor overige exceptions
            System.err.println("Exception error: " + e.getMessage());
        }

//        System.out.println("Get all questions from CSV: ");
//        for (Question q : questionList) {
//            System.out.println(q);
//        }

        dbAccess.closeConnection(); // Netjes weer afsluiten
    } // main

    private static JsonObject[] getAllDocuments() {
        return null;
    }

    // Deze methode wordt alleen aangeroepen wanneer er met een schone DB moet worden gestart
    private static void deleteFromTable(DBAccess dbAccess) {
        // Eerst de database legen om te kunnen testen / demo
        try {
            String sql = "DELETE FROM Question";
            var ps = dbAccess.getConnection().prepareStatement(sql);
            ps.executeUpdate();
            System.out.println(MSG_TABEL_USER_GELEEGD);
        } catch (Exception e) {
            System.err.println(MSG_FOUT_LEEGMAKEN_TABEL + e.getMessage());
        }
    } // deleteFromTable

    // Methode voor het opslaan van CouchDBQuestionList
    public static void saveCouchDBQuestionList(List<Question> questions) {
        if (couchDBAccess.getClient() != null) {
            System.out.println("Connectie open");
            for (Question question : questions) {
                questionCouchDBDAO.storeOne(question);
            }
        }
    } // saveCouchDBQuestionList

} // LauncherVanRonald
