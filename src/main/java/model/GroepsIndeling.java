package model;

public class GroepsIndeling {
    private Group group;
    private Course course;
    private User user;

    public GroepsIndeling(Group group, Course course, User user) {
        this.group = group;
        this.course = course;
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s\n", getUser().getUserName(),getCourse().getCourseName(),getGroup().getGroupName());
    }
}
