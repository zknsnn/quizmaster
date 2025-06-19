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

    // "Student" in plaats van STUDENT Toegang tot weergavenaam
    public String getDisplayName() {
        return displayName;
    }

    // voor gebuik in GUI
    @Override
    public String toString() {
        return displayName;
    }

    // inconsequente schrijfwijze opgevangen
    public static UserRole fromDisplayName(String displayName) {
        for (UserRole role : UserRole.values()) {
            if (role.getDisplayName().equalsIgnoreCase(displayName.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException(INVALID_DISPLAY_NAME_MSG + displayName);
    }
}
