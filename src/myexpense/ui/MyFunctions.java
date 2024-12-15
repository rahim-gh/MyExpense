package myexpense.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import myexpense.logic.InformationsControl;

public class MyFunctions {
    private int profileId = 1;

    /**
     * This function is responsible for loading the MyExpenseWindow.fxml file and
     * setting up a new stage for the application.
     * It is used to display the main window of the MyExpense application.
     *
     * @param accountId The unique identifier of the user's account.
     */
    public static void loadMyExpense(int accountId) {
        try {
            // Load FXML for main window
            FXMLLoader loader = new FXMLLoader(MyFunctions.class.getResource("ui/MyExpenseWindow.fxml"));
            Parent root = loader.load();

            // Fetching the transaction summary using InformationsControl
            double dailyIncome = InformationsControl.calculateDailyIncome(accountId, 1);
            double dailyExpense = InformationsControl.calculateDailyExpense(accountId, 1);
            double dailyBalance = InformationsControl.calculateDailyBalance(accountId, 1);

            // Accessing the labels directly from the FXML (adjust these as needed based on
            // your FXML file)
            Label incomeLabel = (Label) root.lookup("#incomeLabel");
            Label expenseLabel = (Label) root.lookup("#expenseLabel");
            Label balanceLabel = (Label) root.lookup("#balanceLabel");
            Label profileLabel = (Label) root.lookup("#profileLabel");

            // Setting the daily financial data on the main window
            incomeLabel.setText("Income: " + dailyIncome);
            expenseLabel.setText("Expense: " + dailyExpense);
            balanceLabel.setText("Balance: " + dailyBalance);
            profileLabel.setText("Profile: " + accountId); // Display accountId as profile identifier (or customize as
                                                           // needed)

            // Set up the stage to display the window
            Stage currentStage = new Stage();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("MyExpense - Main");
            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Handle loading exceptions
        }
    }
}
