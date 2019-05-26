package controller;

import app.Main;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Controller
public class AppStartController implements FXMLController
{

    @FXML
    public void newUserButtonOnClick() throws IOException
    {
        Main.openNewScreen("newUser", this, NewUserController.class);
    }

    @FXML
    public void existingUserButtonOnClick() throws IOException
    {
        Main.openNewScreen("existingUser", this, ExistingUserController.class);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
