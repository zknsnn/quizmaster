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
    static DBAccess dbAccess;
    public static void main(String[] args) {
        File groupsFile = new File("src/main/resources/CSV bestanden/Groepen.csv");


        dbAccess = new DBAccess("zgrootrc2", "root", "1707");
        dbAccess.openConnection();

//        importGroupFromCsv(groupsFile);

//        DBAccess dbAccess = Main.getDBAccess();
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);
        GroupDAO groupDAO = new GroupDAO(dbAccess);

//        User user1 = new User("talmama","U#Df;h]$em","Mariella",null,"Talman", UserRole.STUDENT);
        User user2 = new User("knobbse","Xt$G?'Rp%u","Selvi",null,"Knobben", UserRole.COÖRDINATOR);
//        User user3 = new User("lambrge","Mkr<#uG97_","Gertjan",null,"Lambrechts", UserRole.COORDINATOR);
//        User user4 = new User("bruijes","`';q%*Aaua","Esmeralda",null,"Bruijns", UserRole.STUDENT);
        User user5 = new User("stegefa","V<v\"&mBa!w","Fady",null,"Stegehuis", UserRole.DOCENT);
        User user6 = new User("reiniro","J$x3a4s]Ui","Romay",null,"Reinink", UserRole.DOCENT);

//        userDAO.storeOne(user1);
//        userDAO.storeOne(user2);
//        userDAO.storeOne(user3);
//        userDAO.storeOne(user4);
//        userDAO.storeOne(user5);
//        userDAO.storeOne(user6);

        Course course1 = new Course("Python Basis","Beginner",user2);
//        Course course2 = new Course("Java Programming","Beginner",user3);
//        Course course3 = new Course("Python++","Medium",user2);

//        courseDAO.storeOne(course1);
//        courseDAO.storeOne(course2);
//        courseDAO.storeOne(course3);

//        Group group1 = new Group(course1,"groep 1",20,user5);
//        Group group2 = new Group(course2,"groep 2",25,user6);
//        Group group3 = new Group(course3,"groep 3",12,user5);
//
//        groupDAO.storeOne(group1);
//        groupDAO.storeOne(group2);
//        groupDAO.storeOne(group3);


//        List<Course> courseList = courseDAO.getAll();
//
//        for(Course course:courseList){
//            System.out.println(course);
//        }


//        List<Group> groupList = groupDAO.getAll();
//
//
//        for(Group group:groupList){
//            System.out.println(group);
//        }
//
//        System.out.println(groupDAO.getOneByName("groep 1"));
//        System.out.println(groupDAO.getOneByNameAndCourseName("groep 1","sdfd"));

//        groupDAO.deleteGroup("groep 3","Python++");

        Group group4 = new Group(course1,"groep 1", 12,user5);

        groupDAO.updateGroup(group4);

        dbAccess.closeConnection();
    }

    public static void importGroupFromCsv(File groups_file){
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

//    public static void exportToCsv(String filePath){
//            File file = new File(filePath);
//            try{
//                PrintWriter writer = new PrintWriter(file);
//                List<Group> groupList = getAll();
//                for (Group g : groupList) {
//                    writer.printf("%s,%s,%d,%s%n",
//                            g.getCourse().getCourseName(),
//                            g.getGroupName(),
//                            g.getAmount(),
//                            g.getDocent().getUserName()
//                    );
//                }
//            } catch (FileNotFoundException fileError) {
//                System.out.println("File not found ");
//            }
//    }
}
