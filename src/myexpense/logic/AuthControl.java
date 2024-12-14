package myexpense.logic;

import java.util.logging.Level;

import myexpense.database.DBQueries;
import myexpense.utils.ExceptionControl.NotFoundException;
import myexpense.utils.LoggerControl;
import myexpense.utils.PasswordHasher;

public class AuthControl {
    public static int authenticate(String username, String password) throws NotFoundException {
        try {
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
            LoggerControl.logMessage("Unexpected error during authentication or registration: " + e.getMessage(),
                    Level.SEVERE);
            throw new NotFoundException("Authentication or registration failed due to an unexpected error.");
        }
    }

    public static int register(String username, String password) {
        try {
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
