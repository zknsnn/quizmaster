package controller;

import database.mysql.DBAccess;
import view.Main;

public class LauncherVanRonald {
    public static void main(String[] args) {

        DBAccess dbAccess = Main.getDBAccess();
        dbAccess.closeConnection();
    }
}
