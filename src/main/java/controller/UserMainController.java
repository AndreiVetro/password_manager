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
import java.security.SecureRandom;
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
    private int selectedIndex = -1;
    private Password selectedPassword;
    private ObservableList<Password> passwordObservableList;

    @FXML
    public void backButtonOnClick()
    {
        Main.goBackButtonOnClick();
    }

    private char[] password;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-=`/.,;'[]~!@#$%^&*()_+}{}:?><|\"\\";
    private static SecureRandom rnd = new SecureRandom();

    private String getRandomString(int len){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
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
        errorLabel.setVisible(false);
        selectionModel = passwordListView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener(observable ->
        {
            if(!selectionModel.isEmpty())
            {
                selectedPassword = selectionModel.getSelectedItem();
                selectedIndex = selectionModel.getSelectedIndex();
                if(sourceButton == editButton)
                {
                    serviceTextField.setText(String.valueOf(selectedPassword.getService()));
                    passwordTextField.setText(String.valueOf(selectedPassword.getText()));
                }
                System.out.println("selected " + selectedPassword + " " + selectedIndex);
            }
        });

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

        passwordObservableList = passwordListView.getItems();


        addEditHBox.setVisible(false);

        addButton.setOnAction(event ->
        {
            sourceButton = (Button) event.getSource();
            addEditHBox.setVisible(true);
            serviceTextField.clear();
            passwordTextField.clear();
        });

        editButton.setOnAction(event ->
        {
            sourceButton = (Button) event.getSource();

            if(!selectionModel.isEmpty())
            {
                serviceTextField.setText(String.valueOf(selectedPassword.getService()));
                passwordTextField.setText(String.valueOf(selectedPassword.getText()));

                if(addEditHBox.isVisible())
                {
                    addEditHBox.setVisible(false);
                }
                else
                {
                    addEditHBox.setVisible(true);
                }
            }
        });

        generateButton.setOnAction(event -> passwordTextField.setText(getRandomString(20)));

        confirmButton.setOnAction(event ->
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
                else if(sourceButton == addButton)
                {
                    Password newPassword = new Password(password, service.toCharArray(), Main.user);
                    addPassword(newPassword);
                    passwordObservableList.add(newPassword);
                    addEditHBox.setVisible(false);
                }
                else if(sourceButton == editButton)
                {
                    Password updatedPassword = selectedPassword;
                    updatedPassword.setService(service.toCharArray());
                    updatedPassword.setText(password);

                    updatePassword(updatedPassword);
                    addEditHBox.setVisible(false);
                }
        });

        serviceTextField.textProperty().addListener((observable) -> errorLabel.setVisible(false));
        passwordTextField.textProperty().addListener(observable -> errorLabel.setVisible(false));

        removeButton.setOnAction(event ->
        {
            if(!selectionModel.isEmpty())
            {
                System.out.println(selectedPassword);
                removePassword(selectedPassword);
                passwordObservableList.remove(selectedPassword);

                if(passwordObservableList.size() > 0)
                {
                    selectedPassword = passwordListView.getFocusModel().getFocusedItem();
                    selectedIndex = passwordListView.getFocusModel().getFocusedIndex();
                }

                if(addEditHBox.isVisible())
                {
                    addEditHBox.setVisible(false);
                }
            }
        });

    }

}
