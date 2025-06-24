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

import java.util.List;

//Hou rekening met het volgende:
//Het label (“Vraag 1” in de figuur) moet worden aangepast om het juiste vraagnummer weer te geven.
//De text area bevat de vraag en de antwoorden. De antwoorden worden daarbij in willekeurige volgorde
// gepresenteerd.
//Door op een van de knoppen [Antwoord A] tot en met [Antwoord D] te klikken wordt het betreﬀende antwoord
// geregistreerd en de volgende vraag getoond.
//Door op [Volgende] te klikken wordt de volgende vraag getoond maar er wordt geen antwoord geregistreerd.
//Door op [Vorige] te klikken wordt de vorige vraag getoond zonder een antwoord te registreren.
//Als een student een vraag twee of meer keer beantwoordt, wordt alleen het laatst gegeven antwoord geregistreerd.
//Als de laatste vraag is beantwoord, of als de student bij de laatste vraag op Volgende klikt wordt
// het feedback scherm getoond.

public class FillOutQuizController {
    User loggedInUser;
    Quiz quiz;
    QuestionDAO questionDAO = new QuestionDAO(Main.getDBAccess());
    int teller = 0;

    @FXML
    private Label titleLabel;
    @FXML
    private TextArea questionArea;
    @FXML
    private Button Vorige;

    public void setup(User user, Quiz quiz) {
        this.loggedInUser = user;
        this.quiz = quiz;

        // Haal lijst met vragen op.
        List<Question> lijstMetVragen = questionDAO.getQuestionsByQuizName(quiz.getQuizName());

        // Er moet een teller zijn die bijhoudt bij welk item in de lijstMetVragen je bent.
        questionArea.setText(lijstMetVragen.get(teller).getQuestionText());


        System.out.println(lijstMetVragen);
        for (Question question : lijstMetVragen) {
            System.out.println(question.getQuestionText());
        }

    }

    public void doRegisterA() {}

    public void doRegisterB() {}

    public void doRegisterC() {}

    public void doRegisterD() {}

    public void doNextQuestion() {
    }

    public void doPreviousQuestion() {
        // iets bedenken waardoor je niet terug kan klikken als de teller bij 0 is.
        if (teller < 0) {
            Vorige.setDisable(true);
        }
    }

    public void doMenu() {
        Main.getSceneManager().showWelcomeScene(loggedInUser);
    }
}
