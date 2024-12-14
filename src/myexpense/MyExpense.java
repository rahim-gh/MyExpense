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
     */
    public static void main(String[] args) {
        /* Init logging */
        LoggerControl.configureLogger();

        /* Init DataBase */
        DBQueries.createTables();

        
    }
}
