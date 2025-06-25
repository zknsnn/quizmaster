package launcher;


import database.couchDB.CouchDBAccess;
import database.couchDB.CourseCouchDBDAO;
import database.mysql.UserDAO;
import model.Course;
import model.User;
import view.Main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CouchDBCourseDAOLauncher {

    private static CouchDBAccess couchDBAccess;
    private static CourseCouchDBDAO courseCouchDBDAO;

    public static void main(String[] args) {

        couchDBAccess = new CouchDBAccess("course", "sinan", "123456");
        courseCouchDBDAO = new CourseCouchDBDAO(couchDBAccess);

        UserDAO userDAO = new UserDAO(Main.getDBAccess());
        Main.getDBAccess().openConnection();

        String csvPath = "src/main/resources/CSV bestanden/Cursussen.csv";

        List<Course> courseList = buildCourseList(csvPath, userDAO);
        saveCourseList(courseList);
    }

    // Lees de CSV file en maak een lijst van Course objecten met bijbehorende User
    public static List<Course> buildCourseList(String csvPath, UserDAO userDAO) {
        List<Course> courseList = new ArrayList<>();
        File file = new File(csvPath);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String courseName = parts[0].trim();
                    String courseDescription = parts[1].trim();
                    String userName = parts[2].trim();


                    User user = userDAO.getOneByName(userName);

                    Course course = new Course(courseName, courseDescription, user);
                    courseList.add(course);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV bestand niet gevonden: " + e.getMessage());
        }

        return courseList;
    }
    // Controleer of de CouchDB connectie open is en sla elke Course op in CouchDB
    private static void saveCourseList(List<Course> courseList){
        if(couchDBAccess.getClient() != null){
            System.out.println("Connection open");
            for (Course course : courseList){
                courseCouchDBDAO.saveSingleCourse(course);
            }
        } else {
            System.out.println("Connection failed.");
        }
    }
}