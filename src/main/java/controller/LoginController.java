package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField nameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    public void doLogin() {
        String name = nameTextField.getText();
        String password = passwordField.getText();

        //check leeg veld
        if(name.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Invoer verplicht!");
            return;
        }

        messageLabel.setText("Inloggen: " + name + ", wachtwoord: " + "*".repeat(password.length()));
        //todo moet nog echte logincontrole
    }

    public void doQuit() {}
}
