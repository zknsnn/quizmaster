package model;

public class Inschrijving {

    User student;
    Course course;


    public Inschrijving(User student, Course courseName) {
        this.student = student;
        this.course = courseName;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return String.format("%s - %s\n",student.getUserName(), course.getCourseName());
    }

}
