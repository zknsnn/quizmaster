package launcher;

import controller.CouchDBAccess;
import database.mysql.*;
import model.Course;
import model.User;
import model.UserRole;
import view.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static launcher.CouchDBCourseDAOLauncher.buildCourseList;

public class LauncherVanSinan {


    public static void main(String[] args) {
        DBAccess dbAccess = Main.getDBAccess();
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        String csvPath = "src/main/resources/CSV bestanden/Cursussen.csv";
        List<Course> courses = buildCourseList(csvPath, userDAO);
        for (Course c : courses) {
            courseDAO.storeOne(c);
        }

        /*DBAccess dbAccess = Main.getDBAccess();

        File coursesBestand = new File("src/main/resources/CSV bestanden/Cursussen.csv");
        List<Course> courses = new ArrayList<>();
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);
        try {
            Scanner input = new Scanner(coursesBestand);
            while (input.hasNextLine()) {
                String[] regelArray = input.nextLine().split(",");
                Course course = new Course(regelArray[0],regelArray[1],userDAO.getOneByName(regelArray[2]));
                courses.add(course);
            }

            for (Course c : courses) {
                courseDAO.storeOne(c);
            }
        } catch (FileNotFoundException fileError) {
            System.out.println("File not found ");
        }*/


    }
}
