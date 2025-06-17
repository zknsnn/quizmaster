/*
 * Etienne
 * DAO (Data Access Object) voor de User.
 * Verantwoordelijk voor: inlezen gebruikers vanuit CSV, ophalen/opslaan gebruikers uit/in database.
 * Implementatie van de GenericDAO interface vereist ook methoden zoals getOneById(), al worden die niet altijd gebruikt.
 */
package database.mysql;

import model.User;
import model.UserRole;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO implements GenericDAO<User> {

    // Foutmeldingen
    public static final String READ_ERROR_MSG = "Fout bij lezen van CSV-bestand: ";
    public static final String LOAD_ERROR_MSG = "Fout bij ophalen van gebruikers: ";
    private static final String FILE_NOT_FOUND_MSG = "Bestand 'Gebruikers.csv' niet gevonden in resources-map.";
    private static final String INVALID_LINE_MSG = "Ongeldige regel in CSV: ";
    private static final String UNKNOWN_ROLE_MSG = "Onbekende rol: ";

    // Verwacht aantal kolommen in CSV-bestand
    private static final int EXPECTED_COLUMN_COUNT = 6;

    public UserDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    /**
     * Inlezen gebruikers uit CSV-bestand.
     * Houdt rekening met aanhalingstekens rond velden (bijv. wachtwoorden met komma's).
     * CSV moet aanwezig zijn in de resources-map.
     */
    public List<User> loadUsersFromCsv() {
        List<User> users = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Gebruikers.csv");

        if (inputStream == null) {
            System.err.println(FILE_NOT_FOUND_MSG);
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = parseCsvLine(line);
                if (parts.size() != EXPECTED_COLUMN_COUNT) {
                    System.err.println(INVALID_LINE_MSG + line);
                    continue;
                }

                String userName = parts.get(0).trim();
                String password = parts.get(1).trim();
                String firstName = parts.get(2).trim();
                String prefix = parts.get(3).trim();
                String lastName = parts.get(4).trim();
                String roleString = parts.get(5).trim();

                try {
                    UserRole userRole = UserRole.valueOf(roleString.toUpperCase());
                    User user = new User(userName, password, firstName, prefix, lastName, userRole);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    System.err.println(UNKNOWN_ROLE_MSG + roleString);
                }
            }
        } catch (Exception e) {
            System.err.println(READ_ERROR_MSG + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Parser voor één CSV-regel, houdt rekening met komma's binnen aanhalingstekens.
     */
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }

    /**
     * Haal alle gebruikers op uit de database.
     */
    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user";
            setupPreparedStatement(sql);
            ResultSet rs = executeSelectStatement();

            while (rs.next()) {
                User user = new User(
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("prefix"),
                        rs.getString("lastName"),
                        UserRole.valueOf(rs.getString("userRol").toUpperCase())
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println(LOAD_ERROR_MSG + e.getMessage());
        }

        return userList;
    }
// CREATE
    @Override
    public void storeOne(User user) {
        try {
            String sql = "INSERT INTO user (userName, password, firstName, prefix, lastName, userRol) VALUES (?, ?, ?, ?, ?, ?)";
            setupPreparedStatementWithKey(sql);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getPrefix());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getUserRol().name());
            executeInsertStatementWithKey();
        } catch (SQLException e) {
            System.err.println("Fout bij opslaan van gebruiker: " + e.getMessage());
        }
    }

   // READ (By Name)
    public User getOneByName(String name) {
        String sql = "SELECT * FROM user WHERE userName = ?";
        User user = null;

        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = executeSelectStatement();

            if (resultSet.next()) {
                UserRole role = UserRole.valueOf(resultSet.getString("userRol").toUpperCase());
                user = new User(
                        resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getString("firstName"),
                        resultSet.getString("prefix"),
                        resultSet.getString("lastName"),
                        role
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL-fout bij ophalen van gebruiker met naam: " + e.getMessage());
        }

        return user;
    }

  // READ (int id)
    @Override
    public User getOneById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;

        try {
            setupPreparedStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = executeSelectStatement();

            if (resultSet.next()) {
                UserRole role = UserRole.valueOf(resultSet.getString("userRol").toUpperCase());
                user = new User(
                        resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getString("firstName"),
                        resultSet.getString("prefix"),
                        resultSet.getString("lastName"),
                        role
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL-fout bij ophalen van gebruiker met ID: " + e.getMessage());
        }
        return user;
    }

    //UPDATE
    // Update de gegevens van een bestaande gebruiker op basis van userId
    public void updateUser(User user) {
        String sql = "UPDATE user SET userName = ?, password = ?, firstName = ?, prefix = ?, lastName = ?, userRol = ? WHERE userId = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getPrefix());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getUserRol().name());
            preparedStatement.setInt(7, user.getUserId());
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij updaten van gebruiker: " + e.getMessage());
        }
    }
    // DELETE
    // Verwijder een gebruiker uit de database op basis van userId
    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE userId = ?";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setInt(1, id);
            executeManipulateStatement();
        } catch (SQLException e) {
            System.err.println("Fout bij verwijderen van gebruiker: " + e.getMessage());
        }
    }

}
