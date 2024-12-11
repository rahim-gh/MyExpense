/**
 * @author rahim
 */
package myexpense.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The `DBConnection` class in Java provides a method `connect()` to establish a
 * database connection using JDBC and returns the connection if successful.
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlite:MyExpense.db"; // SQLite database file
    private static Connection instance = null;

    private DBConnection() {
    }

    /**
     * The getInstance function returns a Connection instance, creating one if it
     * doesn't already exist.
     * 
     * @return The method `getInstance()` is returning a `Connection` object.
     */
    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) { // Reinitialize if null or closed
                instance = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.out.println("Error initializing connection: " + e.getMessage());
        }
        return instance;
    }
}