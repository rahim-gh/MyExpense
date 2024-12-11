/**
 * @author rahim
 */
package myexpense.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import myexpense.utils.LoggerControl;

/**
 * The `DBConnection` class in Java provides a method `getInstance()` to
 * establish a
 * database connection using JDBC and returns the connection if successful.
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlite:MyExpenseDB.db"; // SQLite database file
    private static Connection instance = null;

    private DBConnection() {
    }

    /**
     * The getInstance method returns a Connection instance, creating one if it
     * doesn't already exist or if the previous connection is closed.
     * 
     * @return A `Connection` object if successful, or null if an error occurs.
     */
    public static Connection getInstance() {
        if (instance == null || isConnectionClosed()) { // Check if instance is null or closed
            try {
                instance = DriverManager.getConnection(URL);
                LoggerControl.logMessage("Connected to database", Level.FINEST);
            } catch (SQLException e) {
                LoggerControl.logMessage("Error initializing connection: " + e.getMessage(), Level.SEVERE);
                instance = null; // Set instance to null if connection fails
            }
        }
        return instance;
    }

    /**
     * Helper method to check if the current connection is closed.
     * 
     * @return true if the connection is closed, false otherwise.
     */
    public static boolean isConnectionClosed() {
        try {
            return instance == null || instance.isClosed();
        } catch (SQLException e) {
            LoggerControl.logMessage("Error checking connection status: " + e.getMessage(), Level.SEVERE);
            return true;
        }
    }
}
