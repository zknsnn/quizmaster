
/*
 * Etienne
 * DAO (Data Access Object) voor de User.
 * Inlezen gebruikers uit csv en verbinding gebruikers met database
 * Let op: deze versie gebruikt enkel AbstractDAO als superklasse,
 * GEEN GenericDAO interface.
 * FIX password met komma mogelijk gemaakt
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


//DAO (Data Access Object) User: de Gebruiker

public class UserDAO extends AbstractDAO implements GenericDAO<User> {

    public static final String READ_ERROR_MSG = "Fout bij lezen van CSV-bestand: ";
    public static final String LOAD_ERROR_MSG = "Fout bij ophalen van gebruikers: ";
    // declaratie foutmeldingen en CSV structuur
    private static final int EXPECTED_COLUMN_COUNT = 6;
    private static final String FILE_NOT_FOUND_MSG = "Bestand 'Gebruikers.csv' niet gevonden in resources-map.";
    private static final String INVALID_LINE_MSG = "Ongeldige regel in CSV: ";
    private static final String UNKNOWN_ROLE_MSG = "Onbekende rol: ";

    public UserDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    //inlezen vanuit csv
    // Houdt rekening met komma's binnen aanhalingstekens.
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

    // CSV-regel parser die rekening houdt met aanhalingstekens rond velden.
    // Er bestaat een CSV"Speciaal" maar dat in werking krijgen ben ik nog niet aan toe
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

    //Fetch users
    // rs = ResultSet
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user";
            setupPreparedStatement(sql);
            ResultSet rs = executeSelectStatement();
            while (rs.next()) {
                User user = new User(rs.getString("userName"), rs.getString("password"), rs.getString("firstName"), rs.getString("prefix"), rs.getString("lastName"), UserRole.valueOf(rs.getString("userRol").toUpperCase()));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println(LOAD_ERROR_MSG + e.getMessage());
        }
        return userList;
    }

    // save User in database
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

    @Override
    public User getOneById(int id) {
        System.out.println("Werkt niet in deze versie van UserDAO.getOneById() ");
        return null;
    }

    public void updateUser(User user) {
        // Toekomstige implementatie
    }

    public void deleteUser(int id) {
        // Toekomstige implementatie
    }
}
