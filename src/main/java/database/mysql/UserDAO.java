
/*
 * Etienne
 * DAO (Data Access Object) voor de User.
 * Inlezen gebruikers uit csv en verbinding gebruikers met database
 * Let op: deze versie gebruikt enkel AbstractDAO als superklasse,
 * GEEN GenericDAO interface.
 */
package database.mysql;

import model.User;
import model.UserRole;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class UserDAO extends AbstractDAO {

    // Constants voor foutmeldingen en CSV structuur
    private static final int EXPECTED_COLUMN_COUNT = 6;
    private static final String FILE_NOT_FOUND_MSG = "Bestand 'Gebruikers.csv' niet gevonden in resources-map.";
    private static final String INVALID_LINE_MSG = "Ongeldige regel in CSV: ";
    private static final String UNKNOWN_ROLE_MSG = "Onbekende rol: ";
    public static final String READ_ERROR_MSG = "Fout bij lezen van CSV-bestand: ";
    public static final String LOAD_ERROR_MSG = "Fout bij ophalen van gebruikers: ";

    public UserDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    /**
     * Leest gebruikers in vanuit een CSV-bestand in src/main/resources.
     * @return lijst met User-objecten
     */
    public List<User> loadUsersFromCsv() {
        List<User> users = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Gebruikers.csv");

        if (inputStream == null) {
            System.err.println(FILE_NOT_FOUND_MSG);
            return users;
        }

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != EXPECTED_COLUMN_COUNT) {
                    System.err.println(INVALID_LINE_MSG + line);
                    continue;
                }

                String userName = parts[0].trim();
                String password = parts[1].trim();
                String firstName = parts[2].trim();
                String prefix = parts[3].trim();
                String lastName = parts[4].trim();
                String roleString = parts[5].trim();

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

    //Ophalen gebruikers database.
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

    /**
     * Slaat een gebruiker op in de database.
     */
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

    // Placeholder methods voor toekomstig gebruik tip van Ing
    public void updateUser(User user) {
        // Toekomstige implementatie: update bestaande gebruiker
    }

    public void deleteUser(int id) {
        // Toekomstige implementatie: verwijder gebruiker op basis van ID
    }
}
