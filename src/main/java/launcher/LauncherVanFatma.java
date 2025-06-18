package launcher;

import database.mysql.*;
import model.Course;
import model.Group;
import model.User;
import model.UserRole;
import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LauncherVanFatma {
    public static void main(String[] args) {
        File groupsFile = new File("src/main/resources/CSV bestanden/Groepen.csv");
        importGroupFromCsv(groupsFile);
    }

    public static void importGroupFromCsv(File groups_file){
        DBAccess dbAccess = Main.getDBAccess();
        List<Group> groupList = new ArrayList<>();
        GroupDAO groupDAO = new GroupDAO(dbAccess);
        try {
            String line;
            Scanner scanner = new Scanner(groups_file);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] lineArray = line.split(",");

                UserDAO userDAO = new UserDAO(dbAccess);
                CourseDAO courseDAO = new CourseDAO(dbAccess);
                Course course = courseDAO.getOneByName(lineArray[0]);
                String groupName = lineArray[1];
                int amount = Integer.parseInt(lineArray[2]);
                User teacher = userDAO.getOneByName(lineArray[3]);

                groupList.add(new Group(course,groupName,amount,teacher));
            }

            for (Group group : groupList) {
                groupDAO.storeOne(group);
            }
        } catch (FileNotFoundException fileError) {
            System.out.println("File not found ");
        }
    }
}
