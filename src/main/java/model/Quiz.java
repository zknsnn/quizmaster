package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    //    Een quiz hoort bij een cursus. Een quiz bestaat uit één of meer multiplechoicevragen.
    //    Iedere vraag heeft vier antwoorden. Bij een quiz gebruikt men ook de niveaus. Het niveau
    //    van een quiz mag een niveau lager zijn dan het niveau van de cursus, maar meestal zijn de
    //    niveaus van quiz en cursus gelijk.
    private String quizName;
    private String quizLevel;
    private double succesDefinition;
    private Course course;

    // constructor
    public Quiz(String quizName, String quizLevel, double succesDefinition, Course course) {
        this.quizName = quizName;
        this.quizLevel = quizLevel; // Moet gelijk zijn of een niveau onder course level. Zie setter quiz-level.
        this.succesDefinition = succesDefinition;
        this.course = course;
        List<Question> arrayListQuestions = new ArrayList<>();
    }

    // getters and setters
    public String getQuizLevel() {
        return quizLevel;
    }
    public void setQuizLevel(String quizLevel) {
        this.quizLevel = quizLevel;

        // get course level.

        // check if course level is equal to or one level lower than quiz level.

        // Als dat niet het geval is, zet hem dan op de laag mogelijkste mogelijkheid.
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public double getSuccesDefinition() {
        return succesDefinition;
    }

    public void setSuccesDefinition(double succesDefinition) {
        this.succesDefinition = succesDefinition;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // methoden
    @Override
    public String toString() {
        return String.format("Quiz quiznaam %s, quizLevel = %s, succesDefinition = %f, coursenaam = %s",
                quizName, quizLevel, succesDefinition, course.getCourseName());
    }
} // end Quiz class