package myexpense;

import myexpense.UI.Login;
import myexpense.database.DBQueries;
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

        // Launch the Login window
        Login.launch(args);
    }
}
