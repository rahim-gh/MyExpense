/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
// package myexpense;

// import javafx.application.Application;
// import javafx.stage.Stage;
// import myexpense.database.DBQueries;

// /**
//  * @author rahim
//  */
// public class MyExpense extends Application {

//     /**
//      * @param args the command line arguments
//      */
//     public static void main(String[] args) {
//         /* DataBase init */
//         // Create tables if they don't exist
//         DBQueries.createTables();

//         // Example of inserting a new account
//         //DBQueries.insertAccount("user@example.com", "username", "hashed_password");

//         // Example of retrieving transactions for a profile
//         //DBQueries.getTransactionsByProfile(1);
//         /* End DataBase init */
//     }

//     @Override
//     /**
//      * @param arg0 the command line arguments
//      */
//     public void start(Stage arg0) throws Exception {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'start'");
//     }

// }

package myexpense;

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
     * 
     * @return void
     */
    public static void main(String[] args) {
        LoggerControl.configureLogger();
        LoggerControl.clearLogs();
        // Initialize database and tables
        DBQueries.createTables();

        // Insert a sample account
        // TODO handle multiple accounts with same email or username
        DBQueries.insertAccount("user@example.com", "testuser", "password123");

        System.out.println(DBQueries.getAllAccounts());

        // Add a profile for the account
        // TODO handle multiple profiles with same name
        //DBQueries.insertProfile(1, "Personal");

        // Fetch and display profiles for the account
        System.out.println(DBQueries.getProfilesByAccount(1));

        // Add a transaction for the profile
        //DBQueries.insertTransaction(1, "income", 500.0);

        // Fetch and display transactions for the profile
        System.out.println(DBQueries.getTransactionsByProfile(1));
    }
}
