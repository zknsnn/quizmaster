package model;

public class GroepsIndeling {
    private Group group;
    private User user;
    private Course course;

    public GroepsIndeling(Group group,User user) {
        this.group = group;
        this.user = user;
        this.course = group.getCourse();
//        setCourse(group);
    }

//    public Course getCourse() {
//        return course;
//    }
//
//    public void setCourse(Group group) {
//        this.course = group.getCourse();
//    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s\n", getUser().getUserName(),getGroup().getCourse().getCourseName(),getGroup().getGroupName());
    }
}
