/*
Etienne
ENUM.

ENUM in aparte class,
makkelijker aan te passen / toe te voegen
Voor de verschillende rollen binnen applicatie
rollen bepalen functionaliteit en permissie per gebruiker
 */

package model;

public enum UserRole {
    STUDENT("Student"),
    COÖRDINATOR("Coördinator"),
    FUNCTIONEEL_BEHEERDER("Functioneel beheerder"),
    ADMINISTRATOR("Administrator"),
    DOCENT("Docent");

    public static final String INVALID_DISPLAY_NAME_MSG = "Invalid display name: ";

    // displayName = weergavenaam
    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    // "Student" in plaats van STUDENT – toegang tot weergavenaam
    public String getDisplayName() {
        return displayName;
    }

    // Voor gebruik in GUI
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Maakt van een displayName de bijbehorende enum.
     * Vangt verschillende schrijfwijzen op zoals hoofdletters, underscores en missende trema's.
     * Als geen match wordt gevonden, wordt een IllegalArgumentException gegooid.
     */
    public static UserRole fromDisplayName(String displayName) {
        // Normaliseer invoer (verwijder spaties en maak lowercase)
        String normalized = displayName.trim().toLowerCase();

        switch (normalized) {
            case "student":
            case "studenten": // optioneel alias
                return STUDENT;
            case "coördinator":
            case "coordinator": // zonder trema
                return COÖRDINATOR;
            case "functioneel beheerder":
            case "functioneel_beheerder": // underscore-variant
                return FUNCTIONEEL_BEHEERDER;
            case "administrator":
                return ADMINISTRATOR;
            case "docent":
                return DOCENT;
            default:
                throw new IllegalArgumentException(INVALID_DISPLAY_NAME_MSG + displayName);
        }
    }
}
