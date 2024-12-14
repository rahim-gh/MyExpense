/**
 * @author rahim
 */
package myexpense.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myexpense.database.DBQueries;

public class TransactionControl {
    // Method to insert a transaction
    public int insert(int accountId, int profileId, double amount, String type, String comment) {
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

    public List<Map<String, Object>> getAllById(int accountId, int profileId) {
        return DBQueries.getTransactionsByProfile(accountId, profileId);
    }
}
