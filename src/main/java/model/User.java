

package model;

import util.UserNameGenerator;

import model.UserRole;




public class User {

    private UserRole userRol;

    private String userName;    // gebruikersnaam volgens vast format (5 achternaam + 2 voornaam, lowercase)
    private String password;    // wachtwoord (eventueel versleuteld)
    private String firstName;
    private String prefix;
    private String lastName;

    /**
     * All-args constructor.
     * voor invoer CSV (userName is bekend)
     */
    public User(String userName, String password, String firstName, String prefix, String lastName, UserRole userRol) {

        this.userName = userName.toLowerCase();
        this.password = password;
        this.firstName = capitalize(firstName);
        this.prefix = prefix;
        this.lastName = capitalize(lastName);
        this.userRol = userRol;
    }

    /**
     * Constructor userName.
     * Wordt gebruikt bij het aanmaken van nieuwe gebruikers in applicatie.
     * userName wordt automatisch gegenereerd uit lastName, firstName,
     * volgens het vaste format (5 letters lastName + 2 letters firstName, allemaal lowercase).
     */
    public User(String password, String firstName, String prefix, String lastName, UserRole userRol) {

        // validatie, invoer is verplicht
        if(firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Invoer verplicht. Voer voornaam in");
        }
        if(lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Invoer verplicht. Voer achternaam in");
        }
        if(password == null || password.isBlank()) {
            throw new IllegalArgumentException("Invoer verplicht. Voer wachtwoord in");
        }
        if(userRol == null) {
            throw new IllegalArgumentException("Invoer verplicht. Voer uw rol in");
        }// gaat niet werken
        this.userName = UserNameGenerator.from(firstName, lastName);
        this.password = password;
        this.firstName = capitalize(firstName);
        this.prefix = prefix;
        this.lastName = capitalize(lastName);
        this.userRol = userRol;

    }

    // Getters & Setters


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

    // Eerste letter firstName & lastName naar hoofdletter
    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

}
