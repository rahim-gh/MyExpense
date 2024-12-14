package myexpense.UI;

import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import myexpense.logic.AuthControl;
import myexpense.utils.ExceptionControl.NotFoundException;

public class Login extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label messageLabel;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("MyExpense Login");

        // Username field
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("input-field");

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("input-field");

        // Login button
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> handleLogin());

        // Message label
        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        // Layout
        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                createHeader(),
                usernameField,
                passwordField,
                loginButton,
                messageLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("login-layout");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private Label createHeader() {
        Label header = new Label("MyExpense Login");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);
        return header;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        try {
            // Authenticate or register the user
            int accountId = AuthControl.authenticate(username, password);

            if (accountId == -1) {
                // Failed
                messageLabel.setText("Incorrect password! Account ID: " + accountId);
                messageLabel.setTextFill(Color.RED);
            } else {
                // Success
                messageLabel.setText("Login successful! Account ID: " + accountId);
                messageLabel.setTextFill(Color.GREEN);
                openMainWindow();
            }
        } catch (NotFoundException e) {
            if (showConfirmation("The username does not exist. Do you want to create a new account?")) {
                try {
                    int newAccountId = AuthControl.register(username, password);
                    messageLabel.setText("Account created successfully! Account ID: " + newAccountId);
                    messageLabel.setTextFill(Color.GREEN);
                } catch (Exception ex) {
                    messageLabel.setText("Failed to create account. Please try again.");
                    messageLabel.setTextFill(Color.RED);
                }
            } else {
                messageLabel.setText("Account creation canceled.");
                messageLabel.setTextFill(Color.RED);
            }
        } catch (Exception e) {
            messageLabel.setText("An unexpected error occurred. Please try again.");
            messageLabel.setTextFill(Color.RED);
        }
    }

    private boolean showConfirmation(String text) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("MyExpense Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void openMainWindow() {
        // Close the login window
        primaryStage.close();

        // Launch the main application window
        MainWindow mainWindow = new MainWindow();
        mainWindow.start(new Stage());
    }
}
