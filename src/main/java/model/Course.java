package model;

public class Course {

    private String courseName;
    private String courseLevel;
    private User coordinator ;

    public Course(String courseName, String courseLevel, User coordinator) {

        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.coordinator = coordinator;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s %s\n",courseName,courseLevel,coordinator.getFirstName(),coordinator.getLastName());
    }

    public String getCourseName() {
        return courseName;
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public User getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }
}
