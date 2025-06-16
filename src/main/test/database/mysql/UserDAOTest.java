package database.mysql;

import model.User;
import model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        // DBAccess: vervang door een testdatabase of mock indien nodig
        DBAccess dbAccess = new DBAccess("QuizmasterTest", "testuser", "testpass");
        userDAO = new UserDAO(dbAccess);
    }

    @Test
    void testLoadUsersFromCsv() {
        List<User> users = userDAO.loadUsersFromCsv();

        assertFalse(users.isEmpty(), "CSV-bestand zou gebruikers moeten laden");

        User eersteGebruiker = users.get(0);
        assertNotNull(eersteGebruiker.getUserName(), "Gebruikersnaam mag niet null zijn");
        assertTrue(eersteGebruiker.getPassword().length() > 0, "Wachtwoord moet gevuld zijn");

        // Controleer of er een komma in het wachtwoord zit als testgeval
        if (eersteGebruiker.getUserName().equals("etienne")) {
            assertTrue(eersteGebruiker.getPassword().contains(","), "Wachtwoord moet een komma bevatten");
        }
    }

    @Test
    void testStoreAndRetrieveUser() {
        User user = new User("testuser123", "pa,ss123", "Test", "", "Gebruiker", UserRole.STUDENT);
        userDAO.storeOne(user);

        List<User> users = userDAO.getAll();
        boolean found = users.stream()
                .anyMatch(u -> u.getUserName().equals("testuser123") && u.getPassword().equals("pa,ss123"));

        assertTrue(found, "Nieuwe gebruiker moet worden gevonden in de database");
    }
}
