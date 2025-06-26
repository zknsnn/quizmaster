// Etienne
//MAAKT EERST TABEL USERS LEEG VOOR DEMO
// Doel: CSV-bestand met gebruikers inlezen en veilig wegschrijven naar database

package launcher;

import database.mysql.DBAccess;
import database.mysql.UserDAO;
import model.User;
import model.UserRole;
import view.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LauncherVanEtienne {

    // === LOG- EN FOUTMELDINGEN ALS CONSTANTEN ===
    private static final String MSG_FILE_ERROR = "Fout bij lezen gebruikersbestand: ";
    private static final String MSG_INVALID_LINE = "Ongeldige regel (verwacht 6 velden): ";
    private static final String MSG_USER_SAVED = "Opgeslagen gebruiker: ";
    private static final String MSG_INVALID_ROLE = "Ongeldige rol voor gebruiker: ";
    public static final String MSG_FOUT_LEEGMAKEN_TABEL = "Fout bij leegmaken tabel: ";
    public static final String MSG_TABEL_USER_GELEEGD = "Tabel User geleegd.";

    public static void main(String[] args) {

        // Verbind met de database via Main
        DBAccess dbAccess = Main.getDBAccess();
        dbAccess.openConnection();

        // Eerst de database legen om te kunnen testen / demo
//        extracted(dbAccess);
        // DAO instantie maken
        UserDAO userDAO = new UserDAO(dbAccess);

        // Pad naar CSV-bestand met gebruikers
        File userFile = new File("src/main/resources/CSV bestanden/GebruikersFB.csv");

        // Lees het bestand regel voor regel in
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(",");

                // Verwacht: 6 kolommen per regel
                if (lineSplit.length == 6) {
                    String userName = lineSplit[0].trim();
                    String password = lineSplit[1].trim();
                    String firstName = lineSplit[2].trim();
                    String prefix = lineSplit[3].trim();
                    String lastName = lineSplit[4].trim();
                    String roleText = lineSplit[5].trim();

                    try {
                        // Enum-waarde ophalen uit tekst (bijv. "student" → UserRole.STUDENT)
                        UserRole userRole = UserRole.fromDisplayName(roleText);
                        // Gebruiker aanmaken en opslaan
                        User user = new User(userName, password, firstName, prefix, lastName, userRole);
                        userDAO.storeOne(user);
                        System.out.println(MSG_USER_SAVED + userName);
                    } catch (IllegalArgumentException e) {
                        // Ongeldige rol gevonden in CSV
                        System.err.println(MSG_INVALID_ROLE + userName + " → " + roleText + " " + e.getMessage());
                    }
                } else {
                    // Te weinig/veel kolommen in CSV-regel
                    System.err.println(MSG_INVALID_LINE + line);
                }
            }
        } catch (IOException e) {
            System.err.println(MSG_FILE_ERROR + e.getMessage());
        }

        // Verbreek de databaseverbinding
        dbAccess.closeConnection();

    }

}
