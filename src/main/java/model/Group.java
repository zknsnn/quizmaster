package model;

public class Group {
    public static final int MIN_AMOUNT = 1;
    public static final int MAX_AMOUNT = 25;
    public static final int DEFAULT_AMOUNT = 25;

    private Course course;
    private String groupName;
    private int amount;
    private User docent;

    public Group(Course course, String groupName, int amount, User docent) {
        this.course = course;
        this.groupName = groupName;
        setAmount(amount);
        this.docent = docent;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourseName(Course course) {
        this.course = course;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < MIN_AMOUNT) {
            this.amount = DEFAULT_AMOUNT;
        } else if (amount > MAX_AMOUNT) {
            this.amount = DEFAULT_AMOUNT;
        } else {
            this.amount = amount;
        }
    }

    public User getDocent() {
        return docent;
    }

    public void setDocent(User docent) {
        this.docent = docent;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d studenten) door %s\n",
                course.getCourseName(), groupName, amount, docent.getUserName());
    }
}
