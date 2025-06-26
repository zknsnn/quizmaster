package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizName;
    private String quizLevel;
    private double succesDefinition;
    private Course course;

    // constructor
    public Quiz(String quizName, String quizLevel, double succesDefinition, Course course) {
        this.quizName = quizName;
        this.quizLevel = quizLevel;
        this.succesDefinition = succesDefinition;
        this.course = course;
    }

    public String getQuizLevel() {
        return quizLevel;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // methoden
    @Override
    public String toString() {
        return String.format("%s - %s - %s",
                quizName, quizLevel, course.getCourseName());
    }
} // end Quiz class