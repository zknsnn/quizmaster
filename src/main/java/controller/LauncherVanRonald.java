package controller;

import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import model.Question;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LauncherVanRonald {
    public static void main(String[] args) {

        DBAccess dbAccess = Main.getDBAccess();
        QuestionDAO questionDAO = new QuestionDAO(dbAccess);
        dbAccess.openConnection();

        System.out.println("Get all questions from DB: ");
        List<Question> allQuestionList = questionDAO.getAll();
        for (Question q : allQuestionList) {
            System.out.println(q);
        }

        // Functionaliteit om de meegeleverde csv-bestanden in te lezen.
        File questionFile = new File("src/main/resources/CSV bestanden/Vragen.csv");
        List<Question>  questionList = new ArrayList<>();
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
                Quiz quizName = lineSplit[5];
                // add line by line into questionList
                questionList.add(new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quizName));
            }
        } catch (FileNotFoundException exception) {
            System.out.println("File Not Found");
        } //

        // Functionaliteit om de ingelezen data weg te schrijven in de database.
//         doe iets met doCreateUpdateQuestion

        dbAccess.closeConnection();

    } // main
} // LauncherVanRonald
