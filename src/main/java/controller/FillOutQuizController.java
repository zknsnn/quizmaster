package controller;

import database.mysql.QuestionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Question;
import model.Quiz;
import model.User;
import view.Main;

import java.util.*;

public class FillOutQuizController {
    User loggedInUser;
    Quiz quiz;
    QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());
    int tellerTitel = 1;
    int tellerAntwoorden = 0;
    List<Question> lijstMetVragen;
    List<Integer> lijstAntwoordenGebruiker = new ArrayList<>();
    List<Integer> lijstCorrecteAntwoorden = new ArrayList<>();
    List<String> lijstGeshuffeldeVragen = new ArrayList<>();

    @FXML
    private Label titleLabel;
    @FXML
    private TextArea questionArea;
    @FXML
    private Button Vorige;

    public void setup(User user, Quiz quiz) {
        this.loggedInUser = user;
        this.quiz = quiz;

        // Je moet niet terug kunnen klikken als je bij de eerste vraag bent.
        Vorige.setDisable(true);

        // Haal lijst met vragen op.
        lijstMetVragen = questionDAO.getQuestionsByQuizName(quiz.getQuizName());

        // Shuffle lijsten met vragen en antwoorden
        for (Question question : lijstMetVragen) {
            shuffleQuestion(question);
        }

        // Toon de eerste vraag in het tekstvak
        toonVraag();

        // zorg ervoor dat de lijst met antwoorden die de gebruiker gaat invoeren even groot is.
        for (int i = 0; i < lijstGeshuffeldeVragen.size(); i++) {
            lijstAntwoordenGebruiker.add(0);
        }
    }

    private void shuffleQuestion (Question question) {
        // Initialiseer en vul lijst met foute antwoorden.
        List<String> lijstMetFouteAntwoorden = new ArrayList<>();
        lijstMetFouteAntwoorden.add(question.getWrongAnswer1());
        lijstMetFouteAntwoorden.add(question.getWrongAnswer2());
        lijstMetFouteAntwoorden.add(question.getWrongAnswer3());
        // Shuffle de lijst met foute antwoorden.
        Collections.shuffle(lijstMetFouteAntwoorden);

        // Genereer een random getal dat de plaats van het goede antwoord bepaalt.
        int randomgetal = (new Random()).nextInt(4);

        // Voeg dit getal toe aan de lijst met correcte antwoorden. Zo weet je welk antwoord het juiste is.
        lijstCorrecteAntwoorden.add(randomgetal);
        lijstMetFouteAntwoorden.add(randomgetal, question.getCorrectAnswer());

        // Voeg items van de lijstMetFouteAntwoorden toe aan de lijstGeshuffeldeVragen.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(question.getQuestionText() + "\n");
        stringBuilder.append("A. " + lijstMetFouteAntwoorden.get(0) + "\n");
        stringBuilder.append("B. " + lijstMetFouteAntwoorden.get(1) + "\n");
        stringBuilder.append("C. " + lijstMetFouteAntwoorden.get(2) + "\n");
        stringBuilder.append("D. " + lijstMetFouteAntwoorden.get(3) + "\n");
        lijstGeshuffeldeVragen.add(stringBuilder.toString());
    }

    private void toonVraag () {
        questionArea.setText(lijstGeshuffeldeVragen.get(tellerAntwoorden));
    }

    public void doRegisterA() {
        lijstAntwoordenGebruiker.set(tellerAntwoorden, 0);
        gaNaarvolgendeVraag();
        System.out.println(lijstAntwoordenGebruiker.get(tellerAntwoorden));
    }

    public void doRegisterB() {
        lijstAntwoordenGebruiker.set(tellerAntwoorden, 1);
        gaNaarvolgendeVraag();
    }

    public void doRegisterC() {
        lijstAntwoordenGebruiker.set(tellerAntwoorden, 2);
        gaNaarvolgendeVraag();
    }

    public void doRegisterD() {
        lijstAntwoordenGebruiker.set(tellerAntwoorden, 3);
        gaNaarvolgendeVraag();
    }

    public void doNextQuestion() {
        gaNaarvolgendeVraag();
    }

    private void gaNaarvolgendeVraag() {
        if (tellerTitel < lijstMetVragen.size()) {
            tellerTitel++;
            tellerAntwoorden++;
            titleLabel.setText("Vraag " + tellerTitel);
            Vorige.setDisable(false);
            toonVraag();
        } else {
            Main.getSceneManager().showStudentFeedback(loggedInUser, quiz);
        }
    }

    public void doPreviousQuestion() {
        tellerTitel--;
        tellerAntwoorden--;
        titleLabel.setText("Vraag " + tellerTitel);
        toonVraag();
        if (tellerTitel <= 1) {
            Vorige.setDisable(true);
        }
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }
}