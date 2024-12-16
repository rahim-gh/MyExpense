package myexpense.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myexpense.logic.TransactionControl;

public class AddTransactionController {
    private int profileId = 1; // Default profile ID, should be passed dynamically
    private boolean isSubmitted = false;

    @FXML
    private TextField amountTextField;

    @FXML
    private RadioButton incomeRadioButton;

    @FXML
    private RadioButton expenceRadioButton;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    public static AddTransactionController showAddTransactionWindow(int profileId) {
        try {
            FXMLLoader loader = new FXMLLoader(MyFunctions.class.getResource("/myexpense/ui/addTransaction.fxml"));
            Parent root = loader.load();

            // Verify controller is not null
            AddTransactionController controller = loader.getController();
            if (controller == null) {
                System.err.println("Failed to load controller from FXML");
                return null;
            }

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Transaction");
            dialogStage.setScene(new Scene(root));

            controller.profileId = profileId;

            // Configure button actions
            controller.submitButton.setOnAction(event -> {
                controller.handleSubmit(dialogStage);
            });

            controller.cancelButton.setOnAction(event -> {
                dialogStage.close();
            });

            dialogStage.showAndWait();

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void initialize() {
        // Configure radio buttons to be mutually exclusive
        incomeRadioButton.setToggleGroup(new javafx.scene.control.ToggleGroup());
        expenceRadioButton.setToggleGroup(incomeRadioButton.getToggleGroup());
    }

    private void handleSubmit(Stage stage) {
        // Validate input
        if (validateInput()) {
            // Determine transaction type
            boolean isIncome = incomeRadioButton.isSelected();
            boolean isExpense = expenceRadioButton.isSelected();
            double amount = Double.parseDouble(amountTextField.getText());
            String comment = commentTextArea.getText();

            if (isIncome) {
                TransactionControl.insert(MyFunctions.accountId, 1, amount, "income", comment);
            } else if (isExpense) {
                TransactionControl.insert(MyFunctions.accountId, 1, amount, "expense", comment);
            }

            isSubmitted = true;
            stage.close();
        }
    }

    private boolean validateInput() {
        // Basic input validation
        if (amountTextField.getText().isEmpty()) {
            // Show error dialog or highlight field
            return false;
        }

        if (!incomeRadioButton.isSelected() && !expenceRadioButton.isSelected()) {
            // Show error dialog or highlight radio buttons
            return false;
        }

        try {
            Double.parseDouble(amountTextField.getText());
        } catch (NumberFormatException e) {
            // Show error dialog about invalid amount
            return false;
        }

        return true;
    }

    // Getter to check if transaction was submitted
    public boolean isSubmitted() {
        return isSubmitted;
    }

    // Getter methods to retrieve transaction details if needed
    public double getAmount() {
        return Double.parseDouble(amountTextField.getText());
    }

    public boolean isIncome() {
        return incomeRadioButton.isSelected();
    }

    public String getComment() {
        return commentTextArea.getText();
    }
}