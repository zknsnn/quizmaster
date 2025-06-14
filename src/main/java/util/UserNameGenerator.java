/*
Etienne
Hier de userNameGenerator om userName te kunnen "berekenen"
StringBuilder bron: Liang, geeksforgeeks

 */

package util;

public class UserNameGenerator {

    public static String from(String firstName, String lastName) {
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";

        // naam bewerken en opschonen van andere karakters, spaties
        String lastClean = lastName.replaceAll("[^a-zA-Z]", "");
        String firstClean = firstName.replaceAll("[^a-zA-Z]", "");

        // substring logic berekening / op lengte brengen
        String lastFive = lastClean.length() >= 5 ? lastClean.substring(0, 5) : lastClean;
        String firstTwo = firstClean.length() >= 2 ? firstClean.substring(0, 2) : firstClean;

        //stringbuilding
        // had ook String + String gekund, maar makkelijk uitbreidbaar.
        StringBuilder sb = new StringBuilder();
        sb.append(lastFive);
        sb.append(firstTwo);

        return sb.toString().toLowerCase();

    }

}//end class
