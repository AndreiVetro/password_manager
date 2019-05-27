package controller;

import app.Main;
import domain.Password;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Controller;

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
    private TextField passwordTextField;

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
    private SelectionModel<Password> selectionModel;
    private Password selectedPassword;
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

    private void zeroArray(char[] array)
    {
        synchronized (array)
        {
            Arrays.fill(array, '0');
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        passwordListView.setItems(FXCollections.observableList(getPasswordsCurrentUser()));
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        passwordListView.setOnMouseClicked(event ->
        {


            if(!passwordListView.getSelectionModel().isEmpty())
            {
                content.putString(String.valueOf(passwordListView.getSelectionModel().getSelectedItem().getText()));
                clipboard.setContent(content);
            }

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

            SelectionModel<Password> selectionModel = passwordListView.getSelectionModel();
            if(!selectionModel.isEmpty())
            {
                Password selectedPassword = selectionModel.getSelectedItem();
                serviceTextField.setText(String.valueOf(selectedPassword.getService()));
                passwordTextField.setText(String.valueOf(selectedPassword.getText()));
            }
            addEditHBox.setVisible(true);
        });

        generateButton.setOnAction(event ->
        {
            passwordTextField.setText(getRandomString());
        });


        visiblePressed = false;
        visibleLabel.setOnMouseClicked(event ->
        {
            password = passwordTextField.getText().toCharArray();
            if(visiblePressed)
            {
                passwordTextField.clear();
                passwordTextField.setPromptText(String.valueOf(password));
            }
            else
            {
                passwordTextField.setText(String.valueOf(password));
                visiblePressed = false;
            }
        });


        confirmButton.setOnAction(event ->
        {
            if(sourceButton == addButton)
            {
                String service = serviceTextField.getText();
                char[] password = passwordTextField.getText().toCharArray();

                if(service.isEmpty())
                {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Service can't be empty");
                }
                else if(password.length == 0)
                {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Password can't be empty");
                }
                else
                {
                    Password newPassword = new Password(service.toCharArray(), password, Main.user);
                    addPassword(newPassword);
                    passwordListView.getItems().add(newPassword);
                }

                zeroArray(password);
            }
            else
            {

                //edit stuff
            }

            addEditHBox.setVisible(false);
        });

        serviceTextField.textProperty().addListener((observable) -> errorLabel.setVisible(false));
        passwordTextField.textProperty().addListener(observable -> errorLabel.setVisible(false));

        removeButton.setOnAction(event ->
        {
            SelectionModel<Password> passwordSelectionModel = passwordListView.getSelectionModel();
            if(!passwordSelectionModel.isEmpty())
            {
                Password password = passwordSelectionModel.getSelectedItem();
                removePassword(password);
                passwordListView.getItems().remove(password);
            }
        });

    }

}
