package controller;

import app.Main;
import domain.Password;
import domain.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.PasswordRepository;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

@Controller
public class UserMainController extends ParentController implements FXMLController
{

    private static Button sourceButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ListView<Password> passwordListView;

    @FXML
    private TextField serviceTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button addButton;

    @FXML
    private HBox addEditHBox;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button generateButton;

    @FXML
    private Label visibleLabel;

    private Boolean visiblePressed;
    @FXML
    public void backButtonOnClick()
    {
        Main.goBackButtonOnClick();
    }

    private char[] password;

    private String getRandomString() {
        byte[] array = new byte[30];
        new Random().nextBytes(array);

        return new String(array, Charset.forName("UTF-8"));
    }

    private void zeroPassword()
    {
        synchronized (password)
        {
            Arrays.fill(password, '0');
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        passwordListView.setItems(FXCollections.observableList(getPasswordsCurrentUser()));
        passwordListView.setOnMouseClicked(event ->
        {

            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(passwordListView.getSelectionModel().getSelectedItem().getText()));
            clipboard.setContent(content);

        });

        addEditHBox.setVisible(false);

        addButton.setOnAction(event ->
        {
            sourceButton = (Button) event.getSource();
            addEditHBox.setVisible(true);
        });

        editButton.setOnAction(event ->
        {
            sourceButton = (Button) event.getSource();
            addEditHBox.setVisible(true);
        });

        generateButton.setOnAction(event ->
        {
            passwordField.setText(getRandomString());
        });


        visiblePressed = false;
        visibleLabel.setOnMouseClicked(event ->
        {
            password = passwordField.getText().toCharArray();
            if(visiblePressed)
            {
                passwordField.clear();
                passwordField.setPromptText(String.valueOf(password));
            }
            else
            {
                passwordField.setText(String.valueOf(password));
            }
        });


        confirmButton.setOnAction(event ->
        {
            Button actionButton = (Button) event.getSource();
            if(actionButton == addButton)
            {
                errorLabel.setVisible(true);
                String service = serviceTextField.getText();

                if(service.isEmpty())
                {
                    errorLabel.setText("Service can't be empty");
                }
                else if(passwordField.textProperty().isEmpty().get())
                {
                    errorLabel.setText("Password can't be empty");
                }

            }
            else
            {
                //edit stuff
            }

            addEditHBox.setVisible(false);
        });

    }

}
