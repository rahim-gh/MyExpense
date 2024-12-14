package myexpense.logic;

public class Currency {

    // Method to convert an amount from one currency to another based on the
    // provided exchange rate
    public static double convertCurrency(double amount, double exchangeRate) {
        // Perform the conversion
        return amount * exchangeRate;
    }

}
