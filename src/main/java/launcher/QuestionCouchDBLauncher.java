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
//
////	CouchDB invoer:
////		Lees de Json-string in het tekstbestand, maak er objecten van en sla die op in CouchDB
//        File questionFile = new File("src/main/resources/CSV bestanden/Vragen.csv");
////        List<Question> questionList = new ArrayList<>();
//        try {
//            String line;
//            Scanner scanner = new Scanner(questionFile);
//            while (scanner.hasNextLine()) {
//                line = scanner.nextLine();
//                String[] lineSplit = line.split(";");
//                String questionText = lineSplit[0];
//                String correctAnswer = lineSplit[1];
//                String wrongAnswer1 = lineSplit[2];
//                String wrongAnswer2 = lineSplit[3];
//                String wrongAnswer3 = lineSplit[4];
//                String quizName = lineSplit[5];
//                Quiz quiz = quizDAO.getQuizPerID(quizName); // QuizDAO - quizByName
//                // add line by line into questionList
//                // questionList.add(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz));
//                questionCouchDBDAO.storeOne(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz));
//            }
//        } catch (IOException e) {
//            // Afhandeling voor IOException
//            System.err.println("IOException error: " + e.getMessage());
//        } catch (Exception e) {
//            // Algemene afhandeling voor overige exceptions
//            System.err.println("Exception error: " + e.getMessage());
//        }
//


        //  CouchDB uitvoer:
        //  Overzicht vragen Als coördinator wil ik een tekstbestand kunnen maken met een helder
        //  overzicht van alle vragen per quiz, van alle quizzen in mijn cursussen

//        public Verbruiker getVerbruiker(String postcode, int huisnummer) {
//            // Haal alle documenten op, in de vorm van JsonObjecten; zet om naar Verbruiker en test op postcode-huisnummer
//            Verbruiker resultaat;
//            for (JsonObject jsonObject : getAllDocuments()) {
//                resultaat = gson.fromJson(jsonObject, Verbruiker.class);
//                if (resultaat.getPostcode().equals(postcode)  && (resultaat.getHuisnummer() == huisnummer)) {
//                    return resultaat;
//                }
//            }
//            return null;
//        }

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
    }
}
