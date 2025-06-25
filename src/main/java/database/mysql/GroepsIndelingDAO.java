package database.mysql;

import model.Course;
import model.GroepsIndeling;
import model.Group;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroepsIndelingDAO extends AbstractDAO implements GenericDAO<GroepsIndeling>{

    private UserDAO userDAO;
    private GroupDAO groupDAO;

    public GroepsIndelingDAO(DBAccess dbAccess) {
        super(dbAccess);
        userDAO = new UserDAO(dbAccess);
        groupDAO = new GroupDAO(dbAccess);
    }

    @Override
    public List<GroepsIndeling> getAll() {
        String sql = "SELECT * FROM GroepsIndeling";
        List<GroepsIndeling> groepsIndelingList = new ArrayList<>();
        GroepsIndeling groepsIndeling;
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()){
                String groupName = resultSet.getString("groupName");
                String courseName = resultSet.getString("courseName");
                String studentName = resultSet.getString("userName");
                Group group = groupDAO.getOneByNameAndCourseName(groupName,courseName);
//                Course course = courseDAO.getOneByName(courseName);
                User student = userDAO.getOneByName(studentName);
                groepsIndeling = new GroepsIndeling(group,student);
                groepsIndelingList.add(groepsIndeling);
            }
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return groepsIndelingList;
    }

    public List<User> getAllByCourseNameAndGroupName(String group_name, String course_name){
        String sql = "SELECT * FROM GroepsIndeling WHERE groupName = ? AND courseName = ?";
//        GroepsIndeling groepsIndeling;
        List<User> groepsIndelingStudentList = new ArrayList<>();
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, group_name);
            preparedStatement.setString(2, course_name);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
//                String groupName = resultSet.getString("groupName");
//                String courseName = resultSet.getString("courseName");
                String studentName = resultSet.getString("userName");
//                Group group = groupDAO.getOneByNameAndCourseName(groupName,courseName);
//                Course course = courseDAO.getOneByName(courseName);
                User student = userDAO.getOneByName(studentName);
//                groepsIndeling = new GroepsIndeling(group,course,student);
                groepsIndelingStudentList.add(student);
            }
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return groepsIndelingStudentList;
    }

    @Override
    public GroepsIndeling getOneById(int id) {
        return null;
    }

    @Override
    public void storeOne(GroepsIndeling groepsIndeling) {
        String sql = "INSERT INTO GroepsIndeling (groupName,courseName,userName) VALUES (?,?,?);";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, groepsIndeling.getGroup().getGroupName());
            preparedStatement.setString(2, groepsIndeling.getGroup().getCourse().getCourseName());
            preparedStatement.setString(3, groepsIndeling.getUser().getUserName());
            executeManipulateStatement();
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
    }

    public void deleteOne(GroepsIndeling groepsIndeling){
        String sql = "DELETE FROM GroepsIndeling WHERE groupName = ? AND courseName = ? AND userName = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, groepsIndeling.getGroup().getGroupName());
            preparedStatement.setString(2, groepsIndeling.getGroup().getCourse().getCourseName());
            preparedStatement.setString(3, groepsIndeling.getUser().getUserName());
            executeManipulateStatement();
        } catch (SQLException e) {
            System.out.println("SQL fout bij verwijderen: " + e.getMessage());
        }
    }
}
