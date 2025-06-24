//Etienne
//met update userid


package model;

import util.UserNameGenerator;
import model.UserRole;

public class User {

    private int userId;            // unieke sleutel uit de database
    private UserRole userRol;

    private String userName;       // gebruikersnaam volgens vast format (5 achternaam + 2 voornaam, lowercase)
    private String password;       // wachtwoord (eventueel versleuteld)
    private String firstName;
    private String prefix;
    private String lastName;

    //All args constructor
    public User(int userId, String userName, String password, String firstName, String prefix, String lastName, UserRole userRol) {
        this.userId = userId;
        this.userName = userName.toLowerCase();
        this.password = password;
        this.firstName = capitalize(firstName);
        this.prefix = prefix;
        this.lastName = capitalize(lastName);
        this.userRol = userRol;
    }

   //All args zonder userId
    public User(String userName, String password, String firstName, String prefix, String lastName, UserRole userRol) {
        this.userName = userName.toLowerCase();
        this.password = password;
        this.firstName = capitalize(firstName);
        this.prefix = prefix;
        this.lastName = capitalize(lastName);
        this.userRol = userRol;
    }

    /**
     * Constructor waarbij userName automatisch wordt gegenereerd.
     * Wordt gebruikt bij het aanmaken van nieuwe gebruikers.
     */
//    public User(String password, String firstName, String prefix, String lastName, UserRole userRol) {
//        // validatie
//        if (firstName == null || firstName.isBlank()) {
//            throw new IllegalArgumentException("Invoer verplicht. Voer voornaam in");
//        }
//        if (lastName == null || lastName.isBlank()) {
//            throw new IllegalArgumentException("Invoer verplicht. Voer achternaam in");
//        }
//        if (password == null || password.isBlank()) {
//            throw new IllegalArgumentException("Invoer verplicht. Voer wachtwoord in");
//        }
//        if (userRol == null) {
//            throw new IllegalArgumentException("Invoer verplicht. Voer uw rol in");
//        }
//
//        this.userName = UserNameGenerator.from(firstName, lastName);
//        this.password = password;
//        this.firstName = capitalize(firstName);
//        this.prefix = prefix;
//        this.lastName = capitalize(lastName);
//        this.userRol = userRol;
//    }

    // Getters & Setters

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getUserRol() {
        return userRol;
    }

    public void setUserRol(UserRole userRol) {
        this.userRol = userRol;
    }

    // Eerste letter naar hoofdletter
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return"";//zorgt dat program niet crasht
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userRol=" + userRol +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", prefix='" + prefix + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
