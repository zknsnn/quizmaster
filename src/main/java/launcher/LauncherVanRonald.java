package launcher;

import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import database.mysql.QuizDAO;
import model.Question;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LauncherVanRonald {
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
        } catch (FileNotFoundException exception) {
            System.out.println("File Not Found");
        } //

//        System.out.println("Get all questions from CSV: ");
//        for (Question q : questionList) {
//            System.out.println(q);
//        }



        dbAccess.closeConnection();

    } // main
} // LauncherVanRonald
