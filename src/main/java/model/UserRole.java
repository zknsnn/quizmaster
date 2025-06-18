/*
Etienne
love ENUM.

ENUM in aparte class,
makkelijker aan te passen / toe te voegen
Voor de verschillende rollen binnen applicatie
rollen bepalen functionaliteit en permissie per gebruiker
 */

package model;

public enum UserRole {
    STUDENT("Student"),
    COORDINATOR("Coördinator"),
    FUNCTIONEEL_BEHEERDER("Functioneel beheerder"),
    ADMINISTRATOR("Administrator"),
    DOCENT("Docent");

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
}
