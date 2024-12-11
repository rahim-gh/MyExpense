/**
 * @author rahim
 */
package myexpense.utils;

/**
 * The `ExceptionControl` class provides a centralized location for defining 
 * custom exception types used in the application. These exceptions help 
 * identify and handle specific error scenarios, such as duplicate entries 
 * and authentication failures.
 */
public class ExceptionControl {

    /**
     * The `DuplicateException` class represents an exception that is thrown 
     * when a duplicate entry is detected in the application. 
     * It extends the `Exception` class and provides a constructor for 
     * custom error messages.
     */
    public static class DuplicateException extends Exception {
        public DuplicateException(String message) {
            super(message);
        }
    }

    /**
     * The `AuthenticationException` class represents an exception that is 
     * thrown when an authentication error occurs, such as invalid credentials. 
     * It extends the `Exception` class and provides a constructor for 
     * custom error messages.
     */
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
