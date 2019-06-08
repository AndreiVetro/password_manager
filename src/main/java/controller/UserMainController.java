package controller;

import app.Main;
import domain.Password;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<Password> passwordObservableList;

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
        selectionModel = passwordListView.getSelectionModel();
        passwordObservableList = passwordListView.getItems();

        passwordListView.getSelectionModel().selectedItemProperty().addListener(observable ->
        {
            if(!selectionModel.isEmpty())
            {
                selectedPassword = selectionModel.getSelectedItem();
                System.out.println("selected " + selectedPassword);
                serviceTextField.setText(String.valueOf(selectedPassword.getService()));
                passwordTextField.setText(String.valueOf(selectedPassword.getText()));
            }
        });

        errorLabel.setVisible(false);
        passwordListView.setItems(FXCollections.observableList(getPasswordsCurrentUser()));
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        passwordListView.setOnMouseClicked(event ->
        {
            if(!selectionModel.isEmpty())
            {
                content.putString(String.valueOf(selectedPassword.getText()));
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

            if(!selectionModel.isEmpty())
            {
                serviceTextField.setText(String.valueOf(selectedPassword.getService()));
                passwordTextField.setText(String.valueOf(selectedPassword.getText()));
            }
            if(addEditHBox.isVisible())
            {
                addEditHBox.setVisible(false);
            }
            else
            {
                addEditHBox.setVisible(true);
            }
        });

        generateButton.setOnAction(event -> passwordTextField.setText(getRandomString()));

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
                    Password newPassword = new Password(password, service.toCharArray(), Main.user);
                    addPassword(newPassword);
                    passwordObservableList.add(newPassword);
                }

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
            if(!selectionModel.isEmpty())
            {
                removePassword(selectedPassword);
                passwordObservableList.remove(selectedPassword);
                if(addEditHBox.isVisible())
                {
                    addEditHBox.setVisible(false);
                    if(passwordObservableList.size() == 1)
                    {
                        selectedPassword = passwordObservableList.get(0);
                    }
                }
            }
        });

    }

}
