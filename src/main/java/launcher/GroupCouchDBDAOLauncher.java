package launcher;

import controller.CouchDBAccess;
import database.mysql.CourseDAO;
import database.mysql.UserDAO;
import model.Course;
import model.Group;
import model.User;
import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupCouchDBDAOLauncher {
    private static CouchDBAccess couchDBAccess;
    private static database.mysql.GroupCouchDBDAO groupCouchDBDAO;

    public static void main(String[] args) {

        couchDBAccess = new CouchDBAccess("group", "fatma123", "1707");
        groupCouchDBDAO = new database.mysql.GroupCouchDBDAO(couchDBAccess);

        UserDAO userDAO = new UserDAO(Main.getDBAccess());
        CourseDAO courseDAO = new CourseDAO(Main.getDBAccess());

        String csvPath = "src/main/resources/CSV bestanden/Groepen.csv";

        List<Group> groupList = buildGroupList(csvPath,userDAO,courseDAO);
        saveGroupList(groupList);
    }

    // Lees de CSV file en maak een lijst van Course objecten met bijbehorende User
    public static List<Group> buildGroupList(String csvPath, UserDAO userDAO, CourseDAO courseDAO) {
        List<Group> groupList = new ArrayList<>();
        File file = new File(csvPath);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String courseName = parts[0].trim();
                    String groupName = parts[1].trim();
                    int amount = Integer.parseInt(parts[2].trim());
                    String userName = parts[3].trim();

                    User user = userDAO.getOneByName(userName);
                    Course course = courseDAO.getOneByName(courseName);
                    Group group = new Group(course,groupName,amount,user);
                    groupList.add(group);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV bestand niet gevonden: " + e.getMessage());
        }

        return groupList;
    }

    // Controleer of de CouchDB connectie open is en sla elke group op in CouchDB
    private static void saveGroupList(List<Group> groupList){
        if(couchDBAccess.getClient() != null){
            System.out.println("Connection open");
            for (Group group:groupList){
                groupCouchDBDAO.saveSingleGroup(group);
            }
        } else {
            System.out.println("Connection failed.");
        }
    }
}
