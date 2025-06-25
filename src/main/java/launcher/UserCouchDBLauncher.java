package launcher;

import controller.CouchDBAccess;
import database.mysql.UserCouchDbDAO;
import model.User;
import model.UserRole;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Launcher om gebruikers uit Gebruikers.csv in te lezen en op te slaan in CouchDB.
// Daarna alle opgeslagen gebruikers ophalen en tonen in de console.
public class UserCouchDBLauncher {

    public static void main(String[] args) {

        // Stap 1: toegang tot CouchDB instellen
        CouchDBAccess access = new CouchDBAccess("gebruikers", "admin", "admin");
        UserCouchDbDAO userDAO = new UserCouchDbDAO(access);

        try {
            // Stap 2: lees het CSV-bestand vanaf schijf (ontwikkelomgeving)
            File file = new File("src/main/resources/CSV bestanden/Gebruikers.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String regel;

            // Stap 3: CSV-bestand regel voor regel uitlezen
            while ((regel = reader.readLine()) != null) {
                String[] parts = regel.split(",");

                // Verwacht: 6 velden (userName, password, firstName, prefix, lastName, role)
                if (parts.length == 6) {
                    String userName  = parts[0].trim();
                    String password  = parts[1].trim();
                    String firstName = parts[2].trim();
                    String prefix    = parts[3].trim();
                    String lastName  = parts[4].trim();
                    String roleStr   = parts[5].trim();

                    try {
                        // Enum gebruiken voor rol (bijv. STUDENT, COORDINATOR, ADMIN)
                        UserRole role = UserRole.valueOf(roleStr.toUpperCase());

                        // Gebruiker maken en opslaan in CouchDB
                        User user = new User(userName, password, firstName, prefix, lastName, role);
                        userDAO.saveSingleUser(user);
                        System.out.println("Opgeslagen: " + user.getUserName());

                    } catch (IllegalArgumentException e) {
                        // Ongeldige rol in CSV (bijvoorbeeld tikfout)
                        System.out.println("Ongeldige rol '" + roleStr + "' voor gebruiker: " + userName);
                    }

                } else {
                    // Als het aantal velden niet klopt
                    System.out.println("Ongeldige regel (verwacht 6 velden): " + regel);
                }
            }

        } catch (IOException e) {
            // Fout bij openen of lezen van bestand
            System.err.println("Fout bij verwerken van CSV-bestand: " + e.getMessage());
        }

        // Stap 4: Alle gebruikers uit CouchDB ophalen en tonen
        System.out.println("\nGebruikers opgeslagen in CouchDB:");
        List<User> alleGebruikers = new ArrayList<>();

        for (var json : userDAO.getAllDocuments()) {
            User user = new com.google.gson.Gson().fromJson(json, User.class);
            alleGebruikers.add(user);
        }

        for (User u : alleGebruikers) {
            // Toon gebruikersnaam en volledige naam + rol
            System.out.println("- " + u.getUserName() + " (" +
                    u.getFirstName() + " " +
                    (u.getPrefix().isEmpty() ? "" : u.getPrefix() + " ") +
                    u.getLastName() + ") - rol: " + u.getUserRol());
        }
    }
}
