package database.mysql;

import model.Course;
import model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO extends AbstractDAO implements GenericDAO<Course> {

    public CourseDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    @Override
    public List<Course> getAll() {
        UserDAO userDAO = new UserDAO(dbAccess);
        String sql = "SELECT * FROM Course";
        List<Course> courseList = new ArrayList<>();
        Course course ;
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String courseName = resultSet.getString("courseName");
                String courseLevel = resultSet.getString("courseLevel");
                String coordinator = resultSet.getString("userName");
                User user = userDAO.getOneByName(coordinator);
                course = new Course(courseName, courseLevel, user);
                courseList.add(course);
            }
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return courseList;
    }

    @Override
    public void storeOne(Course course) {
        String sql = "INSERT INTO Course(courseName,courseLevel,userName) VALUES (?,?,?) ;";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setString(2, course.getCourseLevel());
            preparedStatement.setString(3, course.getCoordinator().getUserName());
            executeManipulateStatement();
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
    }

    public Course getOneByName(String coursename) {
        String sql = "SELECT * FROM Course WHERE courseName = ?;";
        Course course = null;
        UserDAO userDAO = new UserDAO(dbAccess);
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, coursename);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                String courseName = resultSet.getString("courseName");
                String courseLevel = resultSet.getString("courseLevel");
                String coordinator = resultSet.getString("userName");
                // ik wil usernaam gebruiken, dus heb ik andere class nodig.
                User user = userDAO.getOneByName(coordinator);
                course = new Course(courseName, courseLevel, user);
            }
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return course;
    }

    @Override
    public Course getOneById(int id) {
        return null;
    }
}

