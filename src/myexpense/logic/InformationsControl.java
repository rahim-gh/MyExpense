package myexpense.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import myexpense.database.DBQueries;

public class InformationsControl {

    // Method to calculate total income for the current month
    public static double calculateMonthlyIncome(int accountId, int profileId) {
        double totalIncome = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("income".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.getMonth() == LocalDate.now().getMonth()
                        && transactionDate.getYear() == LocalDate.now().getYear()) {
                    totalIncome += (double) transaction.get("amount");
                }
            }
        }
        return totalIncome;
    }

    // Method to calculate total expense for the current month
    public static double calculateMonthlyExpense(int accountId, int profileId) {
        double totalExpense = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("expense".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.getMonth() == LocalDate.now().getMonth()
                        && transactionDate.getYear() == LocalDate.now().getYear()) {
                    totalExpense += (double) transaction.get("amount");
                }
            }
        }
        return totalExpense;
    }

    // Method to calculate total balance for the current month
    public static double calculateMonthlyBalance(int accountId, int profileId) {
        double totalIncome = calculateMonthlyIncome(accountId, profileId);
        double totalExpense = calculateMonthlyExpense(accountId, profileId);
        return totalIncome - totalExpense;
    }

    // Method to calculate total income for the current day
    public static double calculateDailyIncome(int accountId, int profileId) {
        double totalIncome = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("income".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.equals(LocalDate.now())) {
                    totalIncome += (double) transaction.get("amount");
                }
            }
        }
        return totalIncome;
    }

    // Method to calculate total expense for the current day
    public static double calculateDailyExpense(int accountId, int profileId) {
        double totalExpense = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("expense".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.equals(LocalDate.now())) {
                    totalExpense += (double) transaction.get("amount");
                }
            }
        }
        return totalExpense;
    }

    // Method to calculate total balance for the current day
    public static double calculateDailyBalance(int accountId, int profileId) {
        double totalIncome = calculateDailyIncome(accountId, profileId);
        double totalExpense = calculateDailyExpense(accountId, profileId);
        return totalIncome - totalExpense;
    }

    // Method to calculate total income for the current year
    public static double calculateYearlyIncome(int accountId, int profileId) {
        double totalIncome = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("income".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.getYear() == LocalDate.now().getYear()) {
                    totalIncome += (double) transaction.get("amount");
                }
            }
        }
        return totalIncome;
    }

    // Method to calculate total expense for the current year
    public static double calculateYearlyExpense(int accountId, int profileId) {
        double totalExpense = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("expense".equalsIgnoreCase(transactionType)) {
                LocalDate transactionDate = ((java.sql.Timestamp) transaction.get("transaction_date")).toLocalDateTime()
                        .toLocalDate();
                if (transactionDate.getYear() == LocalDate.now().getYear()) {
                    totalExpense += (double) transaction.get("amount");
                }
            }
        }
        return totalExpense;
    }

    // Method to calculate total balance for the current year
    public static double calculateYearlyBalance(int accountId, int profileId) {
        double totalIncome = calculateYearlyIncome(accountId, profileId);
        double totalExpense = calculateYearlyExpense(accountId, profileId);
        return totalIncome - totalExpense;
    }

    // Method to calculate total income for all time
    public static double calculateTotalIncome(int accountId, int profileId) {
        double totalIncome = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("income".equalsIgnoreCase(transactionType)) {
                totalIncome += (double) transaction.get("amount");
            }
        }
        return totalIncome;
    }

    // Method to calculate total expense for all time
    public static double calculateTotalExpense(int accountId, int profileId) {
        double totalExpense = 0.0;
        List<Map<String, Object>> transactions = DBQueries.getTransactionsByProfile(accountId, profileId);

        for (Map<String, Object> transaction : transactions) {
            String transactionType = (String) transaction.get("transaction_type");
            if ("expense".equalsIgnoreCase(transactionType)) {
                totalExpense += (double) transaction.get("amount");
            }
        }
        return totalExpense;
    }

    // Method to calculate total balance for all time
    public static double calculateTotalBalance(int accountId, int profileId) {
        double totalIncome = calculateTotalIncome(accountId, profileId);
        double totalExpense = calculateTotalExpense(accountId, profileId);
        return totalIncome - totalExpense;
    }

}
