/**
 * @author rahim
 */
package myexpense.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The `LoggerControl` class in Java sets up a file logger with specified log
 * file path and allows logging messages at different levels.
 */
public class LoggerControl {

    private static Logger logger = Logger.getLogger(LoggerControl.class.getName());

    /**
     * The function `configureLogger` sets up a file logger in Java to log messages
     * to a specified log file path.
     */
    public static void configureLogger() {
        try {
            // Create the /logs directory if it doesn't exist
            java.nio.file.Path logPath = java.nio.file.Paths.get("logs");
            if (!java.nio.file.Files.exists(logPath)) {
                java.nio.file.Files.createDirectories(logPath);
            }

            // Set up the FileHandler for the specified log file path
            FileHandler fileHandler = new FileHandler("logs/MyExpense.log", true); // 'true' for appending logs

            // Create a simple formatter
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Add the FileHandler to the logger
            logger.addHandler(fileHandler);

            // Set log level to INFO (or any other level you prefer)
            //logger.setLevel(Level.WARNING);

        } catch (IOException e) {
            logger.severe("Error setting up file logger: " + e.getMessage());
        }
    }

    /**
     * The function `logMessage` logs a message with a specified level using a
     * logger based on the provided level.
     * 
     * @param message The `message` parameter is a string that contains the
     *                information or content that you want to log. It could be an
     *                error message, a warning, an informational message, or any
     *                other relevant text that you want to record in the log.
     * @param level   The `level` parameter in the `logMessage` method is of type
     *                `Level`. It is used to specify the logging level for the
     *                message being logged. The `Level` class typically represents a
     *                logging level such as SEVERE, WARNING, INFO, CONFIG, FINE,
     *                FINER,
     */
    public static void logMessage(String message, Level level) {
        if (message != null && !message.isEmpty()) {
            // Log the message based on the provided level
            switch (level.getName()) {
                case "SEVERE":
                    logger.severe(message);
                    break;
                case "WARNING":
                    logger.warning(message);
                    break;
                case "INFO":
                    logger.info(message);
                    break;
                case "CONFIG":
                    logger.config(message);
                    break;
                case "FINE":
                    logger.fine(message);
                    break;
                case "FINER":
                    logger.finer(message);
                    break;
                case "FINEST":
                    logger.finest(message);
                    break;
                default:
                    LoggerControl.logMessage("Invalid level: " + level.getName(), Level.WARNING);
                    break;
            }
        } else {
            LoggerControl.logMessage("Invalid message: " + message, Level.WARNING);
        }
    }

    /**
     * The `clearLogs` function clears the contents of a log file by writing an
     * empty string to it.
     */
    public static void clearLogs() {
        try {
            // Define the logs directory path
            Path logsDir = Paths.get("logs");

            // Check if the logs directory exists
            if (Files.exists(logsDir) && Files.isDirectory(logsDir)) {
                // Iterate through all files in the logs directory
                Files.list(logsDir).forEach(file -> {
                    try {
                        // Clear the log file by truncating its content
                        Files.write(file, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
                        logger.fine("Cleared log file: " + file.getFileName());
                    } catch (IOException e) {
                        logger.warning("Error clearing log file: " + file.getFileName() + " - " + e.getMessage());
                    }
                });
            } else {
                logger.warning("Logs directory does not exist or is not a directory.");
            }
        } catch (IOException e) {
            logger.severe("Error accessing logs directory: " + e.getMessage());
        }
    }

    /**
     * The function returns the logger instance.
     * 
     * @return The `logger` object is being returned.
     */
    public static Logger getLogger() {
        return logger;
    }
}
