package myexpense.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import myexpense.logic.InformationsControl;

public class MyFunctions {
    private int profileId = 1;

    // FXML ID for the TextFields
    @FXML
    private TextField balanceTextField;
    @FXML
    private TextField todayBalanceTextField;
    @FXML
    private TextField todayIncomeTextField;
    @FXML
    private TextField todayExpenseTextField;
    @FXML
    private TextField averageDailyIncomeTextField;
    @FXML
    private TextField averageMonthlyIncomeTextField;
    @FXML
    private TextField averageAnnualIncomeTextField;

    // FXML ID for the Button
    @FXML
    private Button addTransactionButton; // Fixed the button variable name

    // FXML ID for the TableView and TableColumns
    @FXML
    private TableView transactionsTable;
    @FXML
    private TableColumn costColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn commentColumn;
    @FXML
    private TableColumn dateColumn;

    public static int accountId;

    /**
     * This function is responsible for loading the MyExpenseWindow.fxml file and
     * setting up a new stage for the application.
     * It is used to display the main window of the MyExpense application.
     *
     * @param accountId The unique identifier of the user's account.
     */
    public static void loadMyExpense(int thisaccountId) {
        accountId = thisaccountId;
        try {
            // Load FXML for main window
            FXMLLoader loader = new FXMLLoader(MyFunctions.class.getResource("/myexpense/ui/MyExpenseWindow.fxml"));
            Parent root = loader.load();

            // Fetching the transaction summary using InformationsControl
            double dailyIncome = InformationsControl.calculateDailyIncome(accountId, 1);
            double dailyExpense = InformationsControl.calculateDailyExpense(accountId, 1);
            double dailyBalance = InformationsControl.calculateDailyBalance(accountId, 1);
            double monthlyIncome = InformationsControl.calculateMonthlyIncome(accountId, 1);
            double annualIncome = InformationsControl.calculateYearlyIncome(accountId, 1);
            double averageDailyIncome = InformationsControl.calculateDailyIncome(accountId, 1);

            // Accessing the TextFields directly from the FXML
            MyFunctions controller = loader.getController();

            // Setting the daily financial data in the TextFields
            controller.balanceTextField.setText(String.valueOf(dailyBalance));
            controller.todayBalanceTextField.setText(String.valueOf(dailyBalance));
            controller.todayIncomeTextField.setText(String.valueOf(dailyIncome));
            controller.todayExpenseTextField.setText(String.valueOf(dailyExpense));
            controller.averageDailyIncomeTextField.setText(String.valueOf(averageDailyIncome));
            controller.averageMonthlyIncomeTextField.setText(String.valueOf(monthlyIncome));
            controller.averageAnnualIncomeTextField.setText(String.valueOf(annualIncome));

            // Set up the stage to display the window
            Stage currentStage = new Stage();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("MyExpense - Main");
            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle the add transaction button click event
    @FXML
    private void addTransactionAction() {
        // Open the Add Transaction window
        AddTransactionController controller = AddTransactionController.showAddTransactionWindow(profileId);

        if (controller != null && controller.isSubmitted()) {
            // Refresh financial information after adding transaction
            double newDailyIncome = InformationsControl.calculateDailyIncome(accountId, 1);
            double newDailyExpense = InformationsControl.calculateDailyExpense(accountId, 1);
            double newBalance = InformationsControl.calculateDailyBalance(accountId, 1);

            // Update the TextFields with the new values
            todayIncomeTextField.setText(String.valueOf(newDailyIncome));
            todayExpenseTextField.setText(String.valueOf(newDailyExpense));
            todayBalanceTextField.setText(String.valueOf(newBalance));
            averageDailyIncomeTextField.setText(String.valueOf(newDailyIncome));
        }
    }
}
