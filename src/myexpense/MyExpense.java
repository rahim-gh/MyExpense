/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myexpense;

import myexpense.database.DBQueries;
import myexpense.utils.ExceptionControl.DuplicateException;
import myexpense.utils.LoggerControl;

/**
 * The class `MyExpense` initializes a database, inserts sample data, fetches
 * and displays profiles and transactions for an account.
 */
public class MyExpense {
    /**
     * The main function initializes a database, inserts sample data, fetches and
     * displays profiles and transactions.
     * 
     * @param args the arguments to be passed to command line
     * @throws DuplicateException
     */
    public static void main(String[] args) {
        /* Init logging */
        LoggerControl.configureLogger();
        LoggerControl.clearLogs();

        /* Init DataBase */
        DBQueries.createTables();

        /*
         * try {
         * DBQueries.insertAccount("user@example.com", "testuser", "password123");
         * } catch (DuplicateException e) {
         * System.out.println("Caught duplicate account");
         * }
         */

        System.out.println(DBQueries.getAllAccounts());

        try {
            DBQueries.insertProfile(1, "Personal");
        } catch (DuplicateException e) {
            System.out.println("Caught duplicate profile");
        }

        System.out.println(DBQueries.getProfilesByAccount(1));

        // DBQueries.insertTransaction(1, "income", 500.0);

        // System.out.println(DBQueries.getTransactionsByProfile(1));
    }
}
