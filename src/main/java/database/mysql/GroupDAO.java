package database.mysql;

import model.Course;
import model.Group;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GroupDAO extends AbstractDAO implements GenericDAO<Group> {
    public GroupDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    public List<Group> getAll() {
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);
        String sql = "SELECT * FROM `Group`";
        List<Group> groupList = new ArrayList<>();
        Group group;
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()){
                String courseName = resultSet.getString("courseName");
                Course course = courseDAO.getOneByName(courseName);
                String groupName = resultSet.getString("groupName");
                int amount = resultSet.getInt("amount");
                String teacher = resultSet.getString("docent");
                User user = userDAO.getOneByName(teacher);
                group = new Group(course,groupName,amount,user);
                groupList.add(group);
            }

        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return groupList;
    }

    @Override
    public Group getOneById(int id) {
        return null;
    }

    //    Omdat deze methode niet tegelijkertijd twee parameters kan aannemen, moest ik het beperken tot één parameter.
//    Aangezien de methode echter een lijst als resultaat kan teruggeven, heb ik tijdelijk een oplossing toegepast waarbij
//    alleen het eerste record uit de resultaten wordt opgehaald. Op deze manier werkt het voorlopig, maar het voldoet uiteraard
//    niet volledig aan de oorspronkelijke bedoeling van de methode.

    public Group getOneByName(String group_name) {
        String sql = "SELECT * FROM `Group` WHERE groupName = ? limit 1;";
        Group group = null;
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, group_name);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String courseName = resultSet.getString("courseName");
                Course course = courseDAO.getOneByName(courseName);
                String groupName = resultSet.getString("groupName");
                int amount = resultSet.getInt("amount");
                String teacher = resultSet.getString("docent");
                User user = userDAO.getOneByName(teacher);
                group = new Group(course, groupName,amount,user);
            }
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return group;
    }

    public Group getOneByNameAndCourseName(String group_name, String course_name){
        String sql = "SELECT * FROM `Group` WHERE groupName = ? AND courseName = ?";
        Group group = null;
        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, group_name);
            preparedStatement.setString(2, course_name);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String courseName = resultSet.getString("courseName");
                Course course = courseDAO.getOneByName(courseName);
                String groupName = resultSet.getString("groupName");
                int amount = resultSet.getInt("amount");
                String teacher = resultSet.getString("docent");
                User user = userDAO.getOneByName(teacher);
                group = new Group(course, groupName,amount,user);
            }
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return group;
    }
    @Override
    public void storeOne(Group group) {
        String sql = "INSERT INTO `Group` (courseName,groupName,amount,docent) VALUES (?,?,?,?);";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, group.getCourse().getCourseName());
            preparedStatement.setString(2, group.getGroupName());
            preparedStatement.setInt(3, group.getAmount());
            preparedStatement.setString(4, group.getDocent().getUserName());
            executeManipulateStatement();
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }

    }

    public void deleteGroup(String groupName, String courseName) {
        String sql = "DELETE FROM `Group` WHERE groupName = ? AND courseName = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, courseName);
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij verwijderen van group: " + e.getMessage());
        }
    }

    public void updateOne(Group group) {
        String sql = "UPDATE `Group` SET amount = ?, docent = ? WHERE courseName = ? AND groupName = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setInt(1, group.getAmount());
            preparedStatement.setString(2, group.getDocent().getUserName());
            preparedStatement.setString(3, group.getCourse().getCourseName());
            preparedStatement.setString(4, group.getGroupName());
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij updaten van group: " + e.getMessage());
        }
    }
}
