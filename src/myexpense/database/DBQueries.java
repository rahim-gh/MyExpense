/**
 * @author rahim
 */
package myexpense.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import myexpense.utils.PasswordHasher;

/**
 * The `DBQueries` class contains methods to create database tables for
 * accounts, profiles, and transactions, insert new accounts, and retrieve
 * transactions by profile.
 */
public class DBQueries {
    private static final Logger logger = Logger.getLogger(DBQueries.class.getName());

    /**
     * The `createTables` function creates three database tables for accounts,
     * profiles, and transactions with specified columns and constraints.
     */
    public static void createTables() {
        String createAccountsTable = """
                    CREATE TABLE IF NOT EXISTS Accounts (
                        account_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        email TEXT NOT NULL UNIQUE,
                        username TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """;

        String createProfilesTable = """
                    CREATE TABLE IF NOT EXISTS Profiles (
                        profile_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        account_id INTEGER NOT NULL,
                        profile_name TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (account_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
                    );
                """;

        String createTransactionsTable = """
                    CREATE TABLE IF NOT EXISTS Transactions (
                        transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        profile_id INTEGER NOT NULL,
                        transaction_type TEXT CHECK(transaction_type IN ('income', 'expense')) NOT NULL,
                        amount DECIMAL(10, 2) NOT NULL,
                        transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (profile_id) REFERENCES Profiles (profile_id) ON DELETE CASCADE
                    );
                """;

        try (Connection conn = DBConnection.getInstance(); Statement stmt = conn.createStatement()) {
            stmt.execute(createAccountsTable);
            stmt.execute(createProfilesTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            logger.severe("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * The `insertAccount` function inserts a new account record into a database
     * table with the provided email, username, and password hash values.
     * 
     * @param email    Email address of the account to be inserted into the
     *                 database.
     * @param username Username of the account to be inserted into the
     *                 database.
     * @param password Hashed password address of the account to be inserted
     *                 into the database.
     */
    public static void insertAccount(String email, String username, String password) {
        String sql = "INSERT INTO Accounts (email, username, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (isDuplicateEntry(conn, email, username)) {
                logger.warning("Duplicate entry detected, account already exists.");
                return;
            } else {
                try {
                    pstmt.setString(1, email);
                    pstmt.setString(2, username);
                    pstmt.setString(3, PasswordHasher.hashPassword(password));

                    pstmt.executeUpdate();
                    logger.info("Record inserted successfully.");
                } catch (SQLException e) {
                    logger.warning("Database error: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
        }
    }

    /**
     * The function `insertProfile` inserts a new profile into a database table
     * using the provided account ID and profile name.
     * 
     * @param accountId   The `accountId` parameter is an integer value representing
     *                    the account ID to which the profile will be associated.
     * @param profileName The `profileName` parameter is a String that represents
     *                    the name of the profile that you want to insert into the
     *                    database.
     */
    public static void insertProfile(int accountId, String profileName) {
        String sql = "INSERT INTO Profiles (account_id, profile_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, profileName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Error inserting profile: " + e.getMessage());
        }
    }

    /**
     * The `insertTransaction` function inserts a new transaction record into a
     * database table with the provided profile ID, transaction type, and amount.
     * 
     * @param profileId       The `profileId` parameter is an integer value
     *                        representing the ID of the user profile for which the
     *                        transaction is being inserted.
     * @param transactionType The `transactionType` parameter in the
     *                        `insertTransaction` method represents the type of
     *                        transaction being recorded. This could be values like
     *                        "deposit", "withdrawal", "transfer", "payment", etc.
     *                        It helps categorize the transaction for reporting and
     *                        analysis purposes.
     * @param amount          The `amount` parameter in the `insertTransaction`
     *                        method represents the monetary value of the
     *                        transaction being inserted into the database. It is of
     *                        type `double`, which is a data type used to store
     *                        decimal numbers. In the context of financial
     *                        transactions, the `amount` parameter typically
     *                        represents the value of
     */
    public static void insertTransaction(int profileId, String transactionType, double amount) {
        String sql = "INSERT INTO Transactions (profile_id, transaction_type, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Error inserting transaction: " + e.getMessage());
        }
    }

    /**
     * Retrieves transactions based on a given profile ID from a
     * database and prints out the transaction details.
     * 
     * @param profileId The SQL query selects all transactions where the
     *                  `profile_id` matches the provided `profileId`.
     */
    public static void getTransactionsByProfile(int profileId) {
        String sql = "SELECT * FROM Transactions WHERE profile_id = ?";
        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String transactionType = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                System.out.printf("Transaction ID: %d, Type: %s, Amount: %.2f%n",
                        transactionId, transactionType, amount);
            }
        } catch (SQLException e) {
            logger.warning("Error fetching transactions: " + e.getMessage());
        }
    }

    /**
     * The function `getProfilesByAccount` retrieves profiles associated with a
     * specific account ID from a database.
     * 
     * @param accountId accountId is the unique identifier of an account for which
     *                  we want to retrieve profiles.
     */
    public static void getProfilesByAccount(int accountId) {
        String sql = "SELECT * FROM Profiles WHERE account_id = ?";
        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int profileId = rs.getInt("profile_id");
                String profileName = rs.getString("profile_name");
                System.out.printf("Profile ID: %d, Name: %s%n", profileId, profileName);
            }
        } catch (SQLException e) {
            logger.warning("Error fetching profiles: " + e.getMessage());
        }
    }

    private static boolean isDuplicateEntry(Connection conn, String email, String username) {
        String query = "SELECT COUNT(*) FROM Accounts WHERE email = ? OR username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, duplicate exists
                }
            }
        } catch (SQLException e) {
            logger.warning("Error checking for duplicates: " + e.getMessage());
        }
        return false;
    }

    // TODO add more
}
