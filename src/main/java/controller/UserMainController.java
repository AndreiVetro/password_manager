package controller;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class UserMainController implements FXMLController
{
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;


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
