package myexpense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myexpense.database.DBQueries;
import myexpense.utils.LoggerControl;

public class MyExpense extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/myexpense/ui/loginWindow.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("MyExpense - Login");
        stage.show();
    }

    public static void main(String[] args) {
        // Init logging
        LoggerControl.configureLogger();

        // Init Database
        DBQueries.createTables();

        // Launch JavaFX
        launch(args);
    }
}
