DBAccess dbAccess = Main.getDBAccess();
try {
    // Hier komen je database operaties
} finally {
    dbAccess.closeConnection(); // Altijd netjes afsluiten
}