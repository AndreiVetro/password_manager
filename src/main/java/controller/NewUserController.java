package controller;

import app.Main;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.UserRepository;
import util.WordUtil;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class NewUserController extends ParentController implements FXMLController
{
    @Autowired
    private WordUtil wordUtil;

    @FXML
    private Button insertSeparatorsButton;

    @FXML
    private Button capitalizeLettersButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField masterPasswordTextField;

    @FXML
    private PasswordField confirmMasterPasswordField;

    @FXML
    private Label passwordsMatchLabel;

    @FXML
    private CheckBox passwordGenerationCheckBox;

    @FXML
    private HBox passwordGenerationHBox;

    @FXML
    private TextField englishWordCountTextField;

    @FXML
    private TextField romanianWordCountTextField;

    @FXML
    private CheckBox includeEnglishWordsCheckBox;

    @FXML
    private CheckBox includeRomanianWordsCheckBox;

    @FXML
    private TextField mustContainTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private HBox generatedPasswordHBox;

    @FXML
    private Label generatedPasswordLabel;

    @FXML
    private HBox englishWordCountHBox;

    @FXML
    private HBox romanianWordCountHBox;

    @FXML
    private Button createButton;

    private final static String PASSWORD_REGEX = "^(?=\\S*[A-Z])(?=\\S*[0-9])(?=\\S*[!@F$%^&*()_+=\\-`~{}\"|';:/?.>,<\\[\\]])\\S*$";

    private List<String> passwordWords;

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
        else if(!masterPasswordTextField.getText().matches(PASSWORD_REGEX))
        {
           errorLabel.setText("A master password must contain at least an uppercase character, a number, a special character and no spaces");
        }
        else if(!masterPasswordTextField.getText().equals(confirmMasterPasswordField.getText()))
        {
            errorLabel.setText("The two passwords must match");
        }
        else
        {
            User user = new User();
            user.setUsername(username);
            user.setMasterPassword(masterPasswordTextField.getText().toCharArray());
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
                createButton.setDisable(false);
            }
            else
            {
                passwordsMatchLabel.setText("Passwords don't match!");
                passwordsMatchLabel.setTextFill(Color.web("#ff0000"));//red
                createButton.setDisable(true);

            }
        }
        else
        {
            passwordsMatchLabel.setText("");
        }
    }

    @FXML
    public void capitalizeLettersButtonOnAction()
    {
        String text = generatedPasswordLabel.getText();
        StringBuilder stringBuilder = new StringBuilder(text);
        do
        {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, text.length());
            if (stringBuilder.substring(randomIndex, randomIndex + 1).matches("[a-z]"))
            {
                stringBuilder.setCharAt(randomIndex, Character.toUpperCase(text.charAt(randomIndex)));
                break;
            }
        } while(stringBuilder.toString().matches(".*[a-z].*"));


        generatedPasswordLabel.setText(stringBuilder.toString());
    }

    @FXML
    public void generatePasswordButtonOnClick()
    {
        passwordWords = new ArrayList<>();
        generatedPasswordLabel.setText("");

        if(includeEnglishWordsCheckBox.isSelected())
        {
            if(!englishWordCountTextField.textProperty().isEmpty().get() && Integer.parseInt(englishWordCountTextField.getText()) > 0)
            {
                passwordWords.addAll(wordUtil.getRandomEnglishWords(Integer.parseInt(englishWordCountTextField.getText())));
            }
        }

        if(includeRomanianWordsCheckBox.isSelected())
        {
            if(romanianWordCountTextField.textProperty().isNotEmpty().get() && Integer.parseInt(romanianWordCountTextField.getText()) > 0)
            {
                passwordWords.addAll(wordUtil.getRandomRomanianWords(Integer.parseInt(romanianWordCountTextField.getText())));
            }
        }

        String mustContainWords = mustContainTextField.getText();
        if(!mustContainWords.isEmpty() && mustContainWords.matches("([a-zA-z0-9]+ *,* *)+"))
        {
            mustContainWords = mustContainWords.replace(" ", "");
            String[] words = mustContainWords.split(",");
            passwordWords.addAll(Arrays.asList(words));
        }

        Collections.shuffle(passwordWords);
        passwordWords.forEach(word -> generatedPasswordLabel.setText(generatedPasswordLabel.getText() + word + " "));

        if(passwordWords.size() > 0)
        {
            insertSeparatorsButton.setDisable(false);
            capitalizeLettersButton.setDisable(false);
        }


    }

    @FXML
    public void insertSeparatorsButtonOnAction()
    {
        String separators = "`~!@#%^&*()-_=+]}[{;:'\",<.>/?";

        if(generatedPasswordLabel.textProperty().isNotEmpty().get())
        {
            String passwordText = generatedPasswordLabel.getText();
            for(int i = 0; i < passwordWords.size() - 1; i++)
            {
                passwordText = passwordText.replaceFirst(" ", String.valueOf(getRandomSeparator(separators)));
            }
            generatedPasswordLabel.setText(passwordText);
        }

        insertSeparatorsButton.setDisable(true);

    }

    private char getRandomSeparator(String separators)
    {
        return separators.charAt(ThreadLocalRandom.current().nextInt(0, separators.length()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        passwordGenerationHBox.setVisible(false);
        generatedPasswordLabel.setText("");
        passwordWords = new ArrayList<>();
        createButton.setDisable(true);
        insertSeparatorsButton.setDisable(true);
        capitalizeLettersButton.setDisable(true);

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
