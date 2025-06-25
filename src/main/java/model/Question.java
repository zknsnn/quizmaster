package model;

public class Question {
    private int questionId;
    private String questionText;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
    private Quiz quiz;

    public Question(int questionId, String questionText, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3, Quiz quiz) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
        this.quiz = quiz;
    } // all-args Question

    public Question(String questionText, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3, Quiz quiz) {
        this(0, questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, quiz);
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getQuestionId() {
        return questionId;
    }

}


