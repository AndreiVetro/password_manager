package controller;

import app.Main;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NewUserController implements FXMLController
{

    @Autowired
    private UserRepository userRepository;


    @FXML
    private HBox passwordGenerationHBox;

    @FXML
    private CheckBox passwordGenerationCheckBox;

    @FXML
    private TextField maximumWordsTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField masterPasswordTextField;

    @FXML
    private PasswordField confirmMasterPasswordField;

    @FXML
    private TextField mustContainTextField;

    @FXML
    private CheckBox includeEnglishWordsCheckBox;

    @FXML
    private CheckBox includeRomanianWordsCheckBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Label passwordsMatchLabel;

    @FXML
    public void createButtonOnClick()
    {
        errorLabel.setVisible(true);
        String username = usernameTextField.getText();

        if(username.isEmpty())
        {
            errorLabel.setText("Username can't be empty");
        }
        else if(userRepository.getByUsername(username) != null)
        {
            errorLabel.setText("Username unavailable");
        }
        else if(masterPasswordTextField.textProperty().isEmpty().get())
        {
            errorLabel.setText("Password can't be empty");
        }
        else if(confirmMasterPasswordField.textProperty().isEmpty().get())
        {
            errorLabel.setText("Please confirm your password");
        }
        else if(masterPasswordTextField.textProperty().length().get() < 12)
        {
            errorLabel.setText("A master password must be at least 12 characters long");
        }
        else if(!masterPasswordTextField.getText().matches(".*"))
        {
           errorLabel.setText("A master password must contain at least an uppercase character, a number and a special symbol");
        }
        else if(!masterPasswordTextField.getText().equals(confirmMasterPasswordField.getText()))
        {
            errorLabel.setText("The two passwords must match");
        }
        else
        {
            User user = new User();
            user.setUsername(username);
            user.setMasterPassword(masterPasswordTextField.getText().toCharArray()); //this gets hashed
            userRepository.add(user);

            Main.goBackButtonOnClick();
        }
    }

    @FXML
    public void backButtonOnClick()
    {
        Main.goBackButtonOnClick();
    }

    private void addPasswordMatchListener()
    {
        String registrationText = masterPasswordTextField.getText();
        String confirmationText = confirmMasterPasswordField.getText();
        if(registrationText.length() > 0 && confirmationText.length() > 0)
        {
            if(registrationText.equals(confirmationText))
            {
                passwordsMatchLabel.setText("Passwords match!");
                passwordsMatchLabel.setTextFill(Color.web("#33cc33"));//green
            }
            else
            {
                passwordsMatchLabel.setText("Passwords don't match!");
                passwordsMatchLabel.setTextFill(Color.web("#ff0000"));//red
            }
        }
        else
        {
            passwordsMatchLabel.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        passwordGenerationHBox.setVisible(false);

        passwordGenerationCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(newValue)
            {
                passwordGenerationHBox.setVisible(true);
            }
            else
            {
                passwordGenerationHBox.setVisible(false);
            }
        }));

        usernameTextField.textProperty().addListener((observable -> errorLabel.setVisible(false)));
        masterPasswordTextField.textProperty().addListener(observable -> errorLabel.setVisible(false));
        confirmMasterPasswordField.textProperty().addListener(observable -> errorLabel.setVisible(false));

        passwordsMatchLabel.setText("");
        masterPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> addPasswordMatchListener());
        confirmMasterPasswordField.textProperty().addListener((observable, oldValue, newValue) -> addPasswordMatchListener());

    }

}
