package controller;

import app.Main;
import domain.Password;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.apache.commons.codec.binary.Base64;


@Controller
public class UserMainController extends ParentController implements FXMLController
{
    @Autowired
    private AES256JNCryptor aes256JNCryptor;

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
    private VBox helpVBox;

    @FXML
    private Label clipboardLabel;



    private Boolean visiblePressed;
    private SelectionModel<Password> selectionModel;
    private int selectedIndex = -1;
    private Password selectedPassword;
    private ObservableList<Password> passwordObservableList;
    private FadeTransition fadeIn = new FadeTransition(Duration.millis(2000));
    private FadeTransition fadeOut = new FadeTransition(Duration.millis(2000));

    @FXML
    public void backButtonOnClick()
    {
        Main.goBackButtonOnClick();
    }


    private static final String alphaNumericSymbolString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-=`/.,;'[]~!@#$%^&*()_+}{}:?><|\"\\";
    private static SecureRandom rnd = new SecureRandom();

    private String getRandomString(){
        StringBuilder sb = new StringBuilder(20);
        for(int i = 0; i < 20; i++)
            sb.append(  alphaNumericSymbolString.charAt(rnd.nextInt(alphaNumericSymbolString.length())));
        return sb.toString();
    }


    private void zeroArray(char[] array)
    {
        synchronized (array)
        {
            Arrays.fill(array, '0');
        }
    }

    @FXML
    public void helpButtonOnClick()
    {
        helpVBox.setVisible(!helpVBox.isVisible());
    }




    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        clipboardLabel.setVisible(false);

        fadeIn.setNode(clipboardLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setNode(clipboardLabel);
        fadeOut.setAutoReverse(false);
        fadeOut.setCycleCount(1);




        helpVBox.setVisible(false);
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
                    passwordTextField.setText(decryptSelectedPassword());
                }
            }
        });

        passwordListView.setItems(FXCollections.observableList(getPasswordsCurrentUser()));
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        passwordListView.setOnMouseClicked(event ->
        {
            if(!selectionModel.isEmpty())
            {
                content.putString(decryptSelectedPassword());
                clipboard.setContent(content);
                clipboardLabel.setVisible(true);
                fadeIn.playFromStart();
                fadeOut.playFromStart();
                //clipboardLabel.setVisible(false);
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
                passwordTextField.setText(decryptSelectedPassword());

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

        generateButton.setOnAction(event -> passwordTextField.setText(getRandomString()));

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
                    try
                    {
                        password = encryptPassword(password);
                    }
                    catch (CryptorException e)
                    {
                        e.printStackTrace();
                    }
                    Password newPassword = new Password(password, service.toCharArray(), Main.user);
                    addPassword(newPassword);
                    passwordObservableList.add(newPassword);
                    addEditHBox.setVisible(false);
                }
                else if(sourceButton == editButton)
                {
                    Password updatedPassword = selectedPassword;
                    updatedPassword.setService(service.toCharArray());
                    try
                    {
                        updatedPassword.setText(encryptPassword(password));
                    }
                    catch (CryptorException e)
                    {
                        e.printStackTrace();
                    }

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

    private String decryptSelectedPassword()
    {
        String text = String.valueOf(selectedPassword.getText());
        byte[] bytes = null;
        try
        {
            bytes = new Base64().decode(text);
            bytes = aes256JNCryptor.decryptData(bytes, Main.user.getMasterPassword());
        }
        catch (CryptorException e)
        {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    private char[] encryptPassword(char[] password) throws CryptorException
    {
        return new Base64().encodeToString(aes256JNCryptor.encryptData(new String(password).getBytes(), Main.user.getMasterPassword())).toCharArray();
    }

}
