/**
 * @author rahim
 */
package myexpense.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.AuthenticationException;

import myexpense.utils.ExceptionControl.DuplicateException;
import myexpense.utils.LoggerControl;
import myexpense.utils.PasswordHasher;

/**
 * The `DBQueries` class contains methods to create database tables for
 * accounts, profiles, and transactions, insert new accounts, and retrieve
 * transactions by profile.
 */
public class DBQueries {
    // private static final Logger logger =
    // Logger.getLogger(DBQueries.class.getName());

    /**
     * The `createTables` function creates three database tables for accounts,
     * profiles, and transactions with specified columns and constraints.
     */
    public static void createTables() {
        String createAccountsTable = """
                    CREATE TABLE IF NOT EXISTS Accounts (
                        account_id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                        comment TEXT NULL,
                        transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (profile_id) REFERENCES Profiles (profile_id) ON DELETE CASCADE
                    );
                """;

        try (Connection conn = DBConnection.getInstance(); Statement stmt = conn.createStatement()) {
            stmt.execute(createAccountsTable);
            stmt.execute(createProfilesTable);
            stmt.execute(createTransactionsTable);
            LoggerControl.logMessage("All tables have been created.", Level.FINEST);
        } catch (SQLException e) {
            LoggerControl.logMessage("Error creating tables: " + e.getMessage(), Level.SEVERE);
        }
    }

    /**
     * The `insertAccount` function inserts a new account record into a database
     * table with the provided email, username, and password hash values.
     * 
     * @param username Username of the account to be inserted into the
     *                 database.
     * @param password Hashed password address of the account to be inserted
     *                 into the database.
     * @return Returns the ID of the created account. -1 in failure.
     * @throws AuthenticationException
     */
    public static int insertAccount(String username, String password)
            throws ArithmeticException, AuthenticationException {
        String checkSql = "SELECT account_id FROM Accounts WHERE username = ?";
        String insertSql = "INSERT INTO Accounts (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance();
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            checkStmt.setString(1, username);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int accountId = rs.getInt(1);
                    String passwordHash = rs.getString(3);

                    if (PasswordHasher.hashPassword(password).equals(passwordHash)) {
                        return accountId;
                    } else {
                        LoggerControl.logMessage("Insert account: wrong pasword.", Level.WARNING);
                        throw new AuthenticationException("Wrong password!");
                    }
                }
            }

            // Insert the new account
            insertStmt.setString(1, username);
            insertStmt.setString(2, PasswordHasher.hashPassword(password));

            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Return the newly generated account ID
                        int accountId = generatedKeys.getInt(1);
                        LoggerControl.logMessage("Account created successfully with ID: " + accountId, Level.FINE);
                        return accountId;
                    }
                }
                LoggerControl.logMessage("Insert successful, but no ID obtained.", Level.FINE);
            } else {
                LoggerControl.logMessage("Insert failed, no rows affected.", Level.WARNING);
            }

        } catch (SQLException e) {
            LoggerControl.logMessage("Insertion account failed: " + e.getMessage(), Level.SEVERE);
        }

        // Return -1 in case of error
        return -1;
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
     * @return Returns the profile ID associated with the account ID. in failure
     *         cases return -1.
     * @throws DuplicateException
     */
    public static int insertProfile(int accountId, String profileName) throws DuplicateException {
        String checkSql = "SELECT profile_id FROM Profiles WHERE account_id = ? AND profile_name = ?";
        String insertSql = "INSERT INTO Profiles (account_id, profile_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance()) {
            // Check for duplicate profile name
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, accountId);
                checkStmt.setString(2, profileName);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        LoggerControl.logMessage("Insertion new profile: Duplicate profile name", Level.INFO);
                        throw new DuplicateException("Insertion new profile: Duplicate profile name");
                    }
                }
            } catch (SQLException e) {
                LoggerControl.logMessage("Error checking profile ID: " + e.getMessage(), Level.SEVERE);
            }

            // Insert new profile
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, accountId);
                insertStmt.setString(2, profileName);
                int affectedRows = insertStmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Return the newly generated ID
                            LoggerControl.logMessage("Profile created successfully.", Level.FINE);
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            LoggerControl.logMessage("Error inserting profile: " + e.getMessage(), Level.WARNING);
        }

        // Return -1 in case of an error or duplication
        return -1;
    }

    /**
     * The `insertTransaction` function inserts a new transaction into a database
     * table and returns the generated transaction ID if successful.
     * 
     * @param accountId       the account id
     * @param profileId       The `profileId` parameter in the `insertTransaction`
     *                        method represents the ID of the user profile for which
     *                        the transaction is being inserted. This ID is used to
     *                        associate the transaction with a specific user profile
     *                        in the database.
     * @param transactionType The `transactionType` parameter in the
     *                        `insertTransaction` method represents the type of
     *                        transaction being inserted into the database. It could
     *                        be a string value such as "income" or "expense",
     *                        indicating the nature of the financial transaction
     *                        being recorded.
     * @param amount          The `amount` parameter in the `insertTransaction`
     *                        method represents the monetary value of the
     *                        transaction being inserted into the database. It is a
     *                        `double` type, which means it can store decimal
     *                        numbers. This parameter is used to specify the amount
     *                        of the transaction, such as the purchase amount,
     *                        transfer
     * @return The method `insertTransaction` returns an integer value, which is the
     *         generated transaction ID if the transaction was inserted
     *         successfully. If the insertion failed or encountered an error, it
     *         returns -1.
     */
    public static int insertTransaction(int accountId, int profileId, String transactionType, double amount,
            String comment) {
        String sql = "INSERT INTO Transactions (account_id, profile_id, transaction_type, amount, comment) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, profileId);
            pstmt.setString(3, transactionType);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, comment);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) { // Get generated keys
                    if (generatedKeys.next()) {
                        LoggerControl.logMessage("Transaction inserted successfully.", Level.FINE);
                        return generatedKeys.getInt(1); // Return the generated transaction ID
                    } else {
                        LoggerControl.logMessage("No ID obtained from auto-increment.", Level.INFO);
                        return -1; // No ID generated
                    }
                }
            } else {
                LoggerControl.logMessage("Transaction insert failed.", Level.INFO);
                return -1; // Insert failed
            }

        } catch (SQLException e) {
            LoggerControl.logMessage("Error inserting transaction: " + e.getMessage(), Level.WARNING);
            return -1; // Return -1 in case of error
        }
    }

    /**
     * The `removeTransaction` function deletes a transaction from the database
     * based on the provided transaction ID.
     * 
     * @param transactionId The ID of the transaction to be removed.
     * @return true if the transaction was successfully removed, false otherwise.
     */
    public static boolean removeTransaction(int accountId, int profileId, int transactionId) {
        String sql = "DELETE FROM Transactions WHERE account_id = ? AND profile_id = ? AND transaction_id = ?";
        try (Connection conn = DBConnection.getInstance();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, profileId);
            pstmt.setInt(3, transactionId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LoggerControl.logMessage("Error removing transaction: " + e.getMessage(), Level.WARNING);
            return false;
        }
    }

    /**
     * The `updateTransaction` function updates an existing transaction in the
     * database
     * with new values based on the provided transaction ID.
     * 
     * @param transactionId   The ID of the transaction to be updated.
     * @param transactionType The new type of transaction (income or expense).
     * @param amount          The new amount for the transaction.
     * @param comment         The new comment for the transaction.
     * @return true if the transaction was successfully updated, false otherwise.
     */
    public static boolean updateTransaction(int accountId, int profileId, int transactionId, String transactionType,
            double amount, String comment) {
        String sql = "UPDATE Transactions SET transaction_type = ?, amount = ?, comment = ? WHERE transaction_id = ? AND account_id = ? AND profile_id = ?";
        try (Connection conn = DBConnection.getInstance();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transactionType);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, comment);
            pstmt.setInt(4, transactionId);
            pstmt.setInt(5, accountId);
            pstmt.setInt(6, profileId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LoggerControl.logMessage("Error updating transaction: " + e.getMessage(), Level.WARNING);
            return false;
        }
    }

    /**
     * return all transaction related to specific profile and account
     * 
     * @param accountId the target account id
     * @param profileId the target profile id
     * @return return a list of transactions
     */
    public static List<Map<String, Object>> getTransactionsByProfile(int accountId, int profileId) {
        List<Map<String, Object>> transactionsList = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE account_id = ? AND profile_id = ?";

        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, profileId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> transactionMap = new HashMap<>();
                    transactionMap.put("profile_id", rs.getInt("profile_id"));
                    transactionMap.put("transaction_id", rs.getInt("transaction_id"));
                    transactionMap.put("transaction_type", rs.getString("transaction_type"));
                    transactionMap.put("amount", rs.getBigDecimal("amount").doubleValue());
                    transactionMap.put("transaction_date", rs.getTimestamp("transaction_date"));

                    transactionsList.add(transactionMap);
                }

            }
        } catch (SQLException e) {
            LoggerControl.logMessage("Error fetching transactions: " + e.getMessage(), Level.WARNING);
        }

        LoggerControl.logMessage("Transactions fetched successfully.", Level.FINE);
        return transactionsList;
    }

    /**
     * The function `getProfilesByAccount` retrieves profiles associated with a
     * specific account from a database and returns them as a map of profile IDs to
     * profile names.
     * 
     * @param accountId accountId is the unique identifier for an account in the
     *                  database. The method `getProfilesByAccount` retrieves
     *                  profiles associated with a specific account based on the
     *                  provided accountId.
     * @return This method returns a `Map<Integer, String>` containing profiles
     *         associated with a specific account. The key of the map is the profile
     *         ID (an integer) and the value is the profile name (a string).
     */
    public static Map<Integer, String> getProfilesByAccount(int accountId) {
        Map<Integer, String> profilesMap = new HashMap<>();
        String sql = "SELECT profile_id, profile_name FROM Profiles WHERE account_id = ?";

        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int profileId = rs.getInt("profile_id");
                    String profileName = rs.getString("profile_name");
                    profilesMap.put(profileId, profileName);
                }
            }
        } catch (SQLException e) {
            LoggerControl.logMessage("Error fetching profiles: " + e.getMessage(), Level.WARNING);
        }

        LoggerControl.logMessage("Profiles fetched successfully", Level.FINE);
        return profilesMap;
    }

    /**
     * This Java function retrieves all accounts from a database table and returns
     * them as a list of maps containing account details.
     * 
     * @return The `getAllAccounts` method returns a List of Maps, where each Map
     *         represents an account with keys "account_id", "email", "username",
     *         and "created_at" mapped to their respective values retrieved from the
     *         database table "Accounts".
     */
    public static List<Map<String, Object>> getAllAccounts() {
        List<Map<String, Object>> accountsList = new ArrayList<>();
        String sql = "SELECT account_id, username, created_at FROM Accounts";

        try (Connection conn = DBConnection.getInstance(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> accountMap = new HashMap<>();
                    accountMap.put("account_id", rs.getInt("account_id"));
                    accountMap.put("username", rs.getString("username"));
                    accountMap.put("created_at", rs.getTimestamp("created_at"));

                    accountsList.add(accountMap);
                }
            }
        } catch (SQLException e) {
            LoggerControl.logMessage("Error fetching accounts: " + e.getMessage(), Level.WARNING);
        }

        LoggerControl.logMessage("Accounts fetched successfully.", Level.FINE);
        return accountsList;
    }

    // TODO add more
}
