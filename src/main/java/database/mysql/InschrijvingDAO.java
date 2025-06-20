package database.mysql;

import model.Course;
import model.Inschrijving;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InschrijvingDAO extends AbstractDAO implements GenericDAO<Inschrijving> {

    public InschrijvingDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    @Override
    public List<Inschrijving> getAll() {

        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        String sql = "SELECT * from Inschrijving";
        List<Inschrijving> inschrijvingList = new ArrayList<>();
        Inschrijving inschrijving ;
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()){
                String userName = resultSet.getString("student");
                User user = userDAO.getOneByName(userName);
                String courseName = resultSet.getString("courseName");
                Course course = courseDAO.getOneByName(courseName);
                inschrijving = new Inschrijving(user,course);
                inschrijvingList.add(inschrijving);
            }
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return inschrijvingList;
    }

    @Override
    public void storeOne(Inschrijving inschrijving) {

        String sql = "INSERT INTO Inschrijving(student,courseName) VALUES (?,?) ;";
        try {
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1,inschrijving.getStudent().getUserName());
            preparedStatement.setString(2,inschrijving.getCourse().getCourseName());
            executeManipulateStatement();
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
    }

    @Override
    public Inschrijving getOneById(int id) {
        return null;
    }

    public List<Inschrijving> getInschrijvingByStudentname(String student) {

        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        String sql = "SELECT * from Inschrijving WHERE student = ?";
        List<Inschrijving> inschrijvingList = new ArrayList<>();
        Inschrijving inschrijving ;
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1,student);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()){
                String userName = resultSet.getString("student");
                User user = userDAO.getOneByName(userName);
                String courseName = resultSet.getString("courseName");
                Course course = courseDAO.getOneByName(courseName);
                inschrijving = new Inschrijving(user,course);
                inschrijvingList.add(inschrijving);

            }
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return inschrijvingList;
    }

    public List<Inschrijving> getInschrijvingByCoursename(String course) {

        UserDAO userDAO = new UserDAO(dbAccess);
        CourseDAO courseDAO = new CourseDAO(dbAccess);

        String sql = "SELECT * from Inschrijving WHERE courseName = ?";
        List<Inschrijving> inschrijvingList = new ArrayList<>();
        Inschrijving inschrijving ;
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1,course);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()){
                String userName = resultSet.getString("student");
                User user = userDAO.getOneByName(userName);
                String courseName = resultSet.getString("courseName");
                Course selCourse = courseDAO.getOneByName(courseName);
                inschrijving = new Inschrijving(user,selCourse);
                inschrijvingList.add(inschrijving);

            }
        }catch (SQLException sqlFout){
            System.out.println("SQL fout " + sqlFout.getMessage());
        }
        return inschrijvingList;
    }

    public void deleteInschrijving(String courseName){
        String sql = "DELETE FROM Inschrijving WHERE courseName = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1,courseName);
            executeManipulateStatement();
        }catch (SQLException e){
            System.out.println("Fout bij verwijderen van inschrijving");
        }
    }

}
