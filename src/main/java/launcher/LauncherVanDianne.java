package launcher;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.QuizDAO;
import model.Course;
import model.Quiz;
import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LauncherVanDianne {
    public static void main(String[] args) {
        DBAccess dbAccess = Main.getDBAccess();
        QuizDAO quizDAO = new QuizDAO(dbAccess);
//        CourseDAO courseDAO;
//        File quizzenBestand = new File("src/main/resources/CSV bestanden/Quizzen.csv");
//        List<Quiz> quizzenLijst = getQuizList(quizzenBestand);
//
//        DBAccess dbAccess = Main.getDBAccess();
//        QuizDAO quizDAO = new QuizDAO(dbAccess);
//
//        for (Quiz quiz : quizzenLijst) {
//            quizDAO.saveQuiz(quiz);
//        }
        ArrayList<Quiz> quizzenLijst = new ArrayList<>();
        quizzenLijst = quizDAO.getAllQuizzes();

        for (Quiz quiz : quizzenLijst) {
            System.out.println(quiz);
        }


    } // einde main

    private static List<Quiz> getQuizList(File quizzenBestand) {
        DBAccess dbAccess = Main.getDBAccess();
        CourseDAO courseDAO = new CourseDAO(dbAccess);
        List<Quiz> quizzenLijst = new ArrayList<>();
        try {
            String line;
            Scanner scanner = new Scanner(quizzenBestand);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                // Split de lijst in elementen
                String[] regelSplit = line.split(",");
                // Sla elk van de elementen op in variabelen.
                String quizName = regelSplit[0];
                String quizLevel = regelSplit[1];
                double succesDefinition = Double.parseDouble((regelSplit[2]));
                String courseName = regelSplit[3];
                // Haal object course op
                Course course = courseDAO.getOneByName(courseName);

                // Plaats objecten in lijst.
                quizzenLijst.add(new Quiz(quizName, quizLevel, succesDefinition, course));
            }
        } catch (FileNotFoundException bestandFout) {
            System.out.println("Bestand niet gevonden");
        }
        return quizzenLijst;
    }
}