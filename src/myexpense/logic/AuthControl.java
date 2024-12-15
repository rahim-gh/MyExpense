package myexpense.logic;

import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import myexpense.database.DBQueries;
import myexpense.utils.ExceptionControl.NotFoundException;
import myexpense.utils.LoggerControl;
import myexpense.utils.PasswordHasher;

public class AuthControl {

    // 5-15 characters, alphanumeric and underscores
    //private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,15}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile(".*");

    // 8-20 characters, at least 1 uppercase, 1 lowercase, 1 digit
    //private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(".*");

    // Method to validate username
    private static boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    // Method to validate password
    private static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Authenticates a user by verifying the username and password.
     *
     * @param username The username of the user. Must be 5-15 characters
     *                 (alphanumeric or underscores).
     * @param password The password of the user. Must be 8-20 characters, include
     *                 uppercase, lowercase, digit, and special character.
     * @return The account ID of the authenticated user if successful. Returns -1 if
     *         the username or password is invalid or incorrect.
     * @throws NotFoundException If the username does not exist in the database.
     * @throws RuntimeException  If an unexpected error occurs during
     *                           authentication.
     */
    public static int authenticate(String username, String password) throws NotFoundException {
        try {
            if (!isValidUsername(username) || !isValidPassword(password)) {
                LoggerControl.logMessage("Authentication failed: Invalid username or password format", Level.WARNING);
                return -1; // Invalid format
            }

            // Check if username exists
            Integer accountId = DBQueries.checkAccount(username);

            if (accountId != null) {
                // Username exists, verify the password
                String storedHash = DBQueries.getPasswordHash(accountId);
                if (storedHash != null && PasswordHasher.verifyPassword(password, storedHash)) {
                    LoggerControl.logMessage("Authentication successful for user " + username, Level.FINE);
                    return accountId; // Successful login
                } else {
                    LoggerControl.logMessage("Authentication failed: Incorrect password for user " + username,
                            Level.WARNING);
                    return -1; // Incorrect password
                }
            } else {
                // Username does not exist, warn the user about account creation
                LoggerControl.logMessage("Username " + username + " not found. Proceeding with account creation.",
                        Level.INFO);
                throw new NotFoundException("Account not found");
            }
        } catch (Exception e) {
            LoggerControl.logMessage("Unexpected error during authentication: " + e.getMessage(),
                    Level.SEVERE);
            throw new RuntimeException("Authentication failed due to an unexpected error.");
        }
    }

    /**
     * Registers a new user by creating an account in the database.
     *
     * @param username The username of the user. Must be 5-15 characters
     *                 (alphanumeric or underscores).
     * @param password The password of the user. Must be 8-20 characters, include
     *                 uppercase, lowercase, digit, and special character.
     * @return The account ID of the newly created user.
     * @throws RuntimeException         If an unexpected error occurs during account
     *                                  creation.
     * @throws IllegalArgumentException If the username or password format is
     *                                  invalid.
     */
    public static int register(String username, String password) throws RuntimeException, IllegalArgumentException {
        try {
            if (!isValidUsername(username)) {
                LoggerControl.logMessage("Registration failed: Invalid username format", Level.WARNING);
                throw new IllegalArgumentException(
                        "Invalid username format.");
            }

            if (!isValidPassword(password)) {
                LoggerControl.logMessage("Registration failed: Invalid password format", Level.WARNING);
                throw new IllegalArgumentException(
                        "Invalid password format");
            }

            // Hash the password
            String hashedPassword = PasswordHasher.hashPassword(password);

            // Insert the account into the database
            int accountId = DBQueries.insertAccount(username, hashedPassword);

            LoggerControl.logMessage("Account created for user " + username, Level.INFO);
            return accountId;
        } catch (Exception e) {
            LoggerControl.logMessage("Unexpected error during account creation: " + e.getMessage(), Level.SEVERE);
            throw new RuntimeException("An unexpected error occurred while creating the account.");
        }
    }

    // show alert popup to confirm user, return the response
    public static Optional<ButtonType> showConfirmPopup(String message) {
        // alert type popup, return the response
        Alert popup = new Alert(AlertType.CONFIRMATION);
        popup.setHeaderText(null);
        popup.setContentText(message);
        return popup.showAndWait();
    }
}
