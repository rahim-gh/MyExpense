/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package myexpense.ui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import myexpense.logic.AuthControl;
import myexpense.utils.ExceptionControl.AuthException;
import myexpense.utils.ExceptionControl.NotFoundException;
import myexpense.utils.LoggerControl;

/**
 *
 * @author Salim Bougrinat
 */
public class LoginWindow implements Initializable {

    @FXML
    private Label feedbackLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;

    @FXML
    private int loginAction(ActionEvent event) throws AuthException {
        String username = usernameTextField.getText().strip();
        String password = passwordPasswordField.getText().strip();

        try {

            int accountId = AuthControl.authenticate(username, password);
            if (accountId != -1) {
                // Successful login, load main window
                MyFunctions.loadMyExpense(accountId);
                LoggerControl.logMessage("Successful login", Level.FINE);
                return accountId; // Successful login, no feedback required
            } else {
                // Password not correct
                feedbackLabel.setTextFill(javafx.scene.paint.Color.RED);
                feedbackLabel.setText("Invalid username or password");
                return -1;
            }

        } catch (NotFoundException e) {
            try {
                Optional<ButtonType> respond = AuthControl
                        .showConfirmPopup("You're about to create a new account, proceed?");
                if (respond.isPresent() && respond.get() == ButtonType.OK) {
                    // User confirmed to proceed
                    int newAccountId = AuthControl.register(username, password);
                    MyFunctions.loadMyExpense(newAccountId);
                    LoggerControl.logMessage("User confirmed to proceed", Level.FINE);
                    return newAccountId;
                } else {
                    // User cancelled
                    feedbackLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                    feedbackLabel.setText("Account creation canceled.");
                    throw new AuthException("Account creation canceled");
                }
            } catch (IllegalArgumentException eIllegalArgument) {
                // Registration failed due to invalid input
                feedbackLabel.setTextFill(javafx.scene.paint.Color.RED);
                feedbackLabel.setText(eIllegalArgument.getMessage());
                return -1;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }
}
