package launcher;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.QuizDAO;
import database.mysql.UserDAO;
import model.Course;
import model.Quiz;
import model.User;
import model.UserRole;
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
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);
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
        User user = new User("Dianne", "Wachtwoord", "Dian", null , "Craats", UserRole.COÖRDINATOR);
        System.out.println(user);
        Course course = new Course("Cavia's Knippen", "Beginner", user);
        System.out.println(course);
        Course courseProbeersel = courseDAO.getOneByName("Analyse");

        Quiz quiz = new Quiz("Awesomequiz4", "Beginner", 70, courseProbeersel);
        System.out.println(quiz);

        quizDAO.saveQuiz(quiz);


        System.out.println(quiz.telAantalVragen(quiz));

        ArrayList<Quiz> quizzenLijst = new ArrayList<>();
        quizzenLijst = quizDAO.getAllQuizzes();

//        for (Quiz quiz : quizzenLijst) {
//            System.out.println(quiz);
//        }
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