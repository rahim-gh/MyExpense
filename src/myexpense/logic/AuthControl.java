package myexpense.logic;

import java.util.logging.Level;
import java.util.regex.Pattern;

import myexpense.database.DBQueries;
import myexpense.utils.ExceptionControl.NotFoundException;
import myexpense.utils.LoggerControl;
import myexpense.utils.PasswordHasher;

public class AuthControl {

    // 5-15 characters, alphanumeric and underscores    
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,15}$");

    // 8-20 characters, at least 1 uppercase, 1 lowercase, 1 digit
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$");

    // Method to validate username
    private static boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    // Method to validate password
    private static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // Authentication method
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

    // Registration method
    public static int register(String username, String password) throws RuntimeException, IllegalArgumentException {
        try {
            if (!isValidUsername(username)) {
                LoggerControl.logMessage("Registration failed: Invalid username format", Level.WARNING);
                throw new IllegalArgumentException(
                        "Invalid username format. Must be 5-15 characters (alphanumeric or underscores).");
            }

            if (!isValidPassword(password)) {
                LoggerControl.logMessage("Registration failed: Invalid password format", Level.WARNING);
                throw new IllegalArgumentException(
                        "Invalid password format. Must be 8-20 characters, include uppercase, lowercase, digit, and special character.");
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
}
