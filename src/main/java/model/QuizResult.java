package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class QuizResult {
    User user;
    Quiz quiz;
    Boolean voldoende;
    LocalDateTime tijdstipQuiz;

    public QuizResult(User user, Quiz quiz, Boolean voldoende, LocalDateTime tijdstipQuiz) {
        this.user = user;
        this.quiz = quiz;
        this.voldoende = voldoende;
        this.tijdstipQuiz = tijdstipQuiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Boolean getVoldoende() {
        return voldoende;
    }

    public void setVoldoende(Boolean voldoende) {
        this.voldoende = voldoende;
    }

    public LocalDateTime getTijdstipQuiz() {
        return tijdstipQuiz;
    }

    public void setTijdstipQuiz(LocalDateTime tijdstipQuiz) {
        this.tijdstipQuiz = tijdstipQuiz;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
