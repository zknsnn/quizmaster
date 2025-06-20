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
    private List<Question> arrayListQuestions;

    // constructor
    public Quiz(String quizName, String quizLevel, double succesDefinition, Course course) {
        this.quizName = quizName;
        setQuizLevel(quizLevel, course);
        this.succesDefinition = succesDefinition;
        this.course = course;
        this.arrayListQuestions = new ArrayList<>();
    }

    // getters and setters
    public List<Question> getArrayListQuestions() {
        return arrayListQuestions;
    }

    public void setArrayListQuestions(List<Question> arrayListQuestions) {
        this.arrayListQuestions = arrayListQuestions;
    }

    public String getQuizLevel() {
        return quizLevel;
    }

    // Het niveau van een quiz mag een niveau lager zijn dan het niveau van de cursus, maar
    // meestal zijn de niveaus van quiz en cursus gelijk
    public void setQuizLevel(String quizLevel, Course course) {
        String courseLevel = course.getCourseLevel();
        // Als het courseLevel "Beginner" is, is dit ook het niveau van de quiz.
        if (courseLevel.equalsIgnoreCase("Beginner")) {
            this.quizLevel = "Beginner";
        }
        // Als het courseLevel "Medium" is, kan quizLevel nooit "Gevorderd" zijn.
        else if (courseLevel.equalsIgnoreCase("Medium")) {
            if (!(quizLevel.equalsIgnoreCase("Gevorderd"))) {
                this.quizLevel = quizLevel;
            } else {
                this.quizLevel = "Medium";
            }
            // Als het courseLevel "Gevorderd" is is quizLevel "Beginner" niet toegestaan".
        } else {
            if (quizLevel.equalsIgnoreCase("Beginner")) {
                this.quizLevel = "Medium";
            } else {
                this.quizLevel = quizLevel;
            }
        }
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
        return String.format("%s - %s - %s",
                quizName, quizLevel, course.getCourseName());
    }

    public int telAantalVragen(Quiz quiz) {
        int numberOfQuestions;
        List<Question> listQuestions = quiz.getArrayListQuestions();
        if (listQuestions.isEmpty()) {
            numberOfQuestions = 0;
        } else {
            numberOfQuestions = listQuestions.size();
        }
        return numberOfQuestions;
    }
} // end Quiz class