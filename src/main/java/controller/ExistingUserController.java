package controller;

import app.Main;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


@Controller
public class ExistingUserController implements FXMLController
{

    @Autowired
    private UserRepository userRepository;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Label errorLabel;


    @FXML
    public void loginButtonOnClick() throws IOException
    {
        String username = usernameTextField.getText();

        if(username.isEmpty())
        {
            errorLabel.setText("Username can't be empty");
        }
        else if(loginPasswordField.textProperty().isEmpty().get())
        {
            errorLabel.setText("Password can't be empty");
        }
        else
        {
            User user = userRepository.getByUsername(username);

            if(user == null || !Arrays.equals(user.getMasterPassword(), loginPasswordField.getText().toCharArray()))
            {
                errorLabel.setText("Invalid login credentials");
            }
            else
            {
                Main.openNewScreen("userMain", this, UserMainController.class);
                //success

            }

        }
    }

    @FXML
    public void backButtonOnClick()
    {
        Main.goBackButtonOnClick();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
