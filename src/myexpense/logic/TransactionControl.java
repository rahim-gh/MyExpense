/**
 * @author rahim
 */
package myexpense.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myexpense.database.DBQueries;

public class TransactionControl {
    // Method to insert a transaction
    public static int insert(int accountId, int profileId, double amount, String type, String comment) {
        return DBQueries.insertTransaction(accountId, profileId, type, amount, comment);
    }

    // Method to remove a transaction by its ID
    public boolean remove(int accountId, int profileId, int transactionId) {
        return DBQueries.removeTransaction(accountId, profileId, transactionId);
    }

    // Method to update a transaction
    public boolean update(int transactionId, double newAmount, String newType, String newComment) {
        return DBQueries.updateTransaction(transactionId, transactionId, transactionId, newType, newAmount, newComment);
    }

    // Method to retrieve a transaction by its ID
    public Map<String, Object> getById(int accountId, int profileId, int transactionId) {
        Map<String, Object> transaction = new HashMap<>();
        transaction = DBQueries.getTransactionsByProfile(accountId, profileId).get(transactionId);

        return transaction;
    }

    // Method to retrieve all transactions by accountId and profileId with optional
    // date filtering
    public List<Map<String, Object>> getAllById(int accountId, int profileId, LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        // If both startDate and endDate are null, return all transactions
        if (startDate == null && endDate == null) {
            return transactions;
        }

        // Filter transactions by date
        List<Map<String, Object>> filteredTransactions = new ArrayList<>();
        for (Map<String, Object> transaction : transactions) {
            LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                    .toLocalDate();

            boolean isAfterStartDate = (startDate == null || !transactionDate.isBefore(startDate));
            boolean isBeforeEndDate = (endDate == null || !transactionDate.isAfter(endDate));

            // Add the transaction if it meets the date filtering conditions
            if (isAfterStartDate && isBeforeEndDate) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

}
