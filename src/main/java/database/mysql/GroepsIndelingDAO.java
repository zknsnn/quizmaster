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
    private CourseDAO courseDAO;

    public GroepsIndelingDAO(DBAccess dbAccess) {
        super(dbAccess);
        userDAO = new UserDAO(dbAccess);
        groupDAO = new GroupDAO(dbAccess);
        courseDAO = new CourseDAO(dbAccess);
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
                Course course = courseDAO.getOneByName(courseName);
                User student = userDAO.getOneByName(studentName);
                groepsIndeling = new GroepsIndeling(group,course,student);
                groepsIndelingList.add(groepsIndeling);
            }
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return groepsIndelingList;
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
            preparedStatement.setString(2, groepsIndeling.getCourse().getCourseName());
            preparedStatement.setString(4, groepsIndeling.getUser().getUserName());
            executeManipulateStatement();
        } catch (SQLException sqlFout) {
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
    }
}
