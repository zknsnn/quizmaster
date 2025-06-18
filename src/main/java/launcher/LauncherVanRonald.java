package launcher;

import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import model.Question;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LauncherVanRonald {

    public static final String MSG_FOUT_LEEGMAKEN_TABEL = "Fout bij leegmaken tabel: ";
    public static final String MSG_TABEL_USER_GELEEGD = "Tabel Question geleegd.";

    public static void main(String[] args) {

        DBAccess dbAccess = Main.getDBAccess();
        QuestionDAO questionDAO = new QuestionDAO(dbAccess);
        QuizDAO quizDAO = new QuizDAO(dbAccess);
        dbAccess.openConnection();

//        System.out.println("Get all questions from DB: ");
//        List<Question> allQuestionList = questionDAO.getAll();
//        for (Question q : allQuestionList) {
//            System.out.println(q);
//        }

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
//                questionList.add(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz));
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



        dbAccess.closeConnection();

    } // main

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
    }
} // LauncherVanRonald
