/**
 * @author rahim
 */
package myexpense.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The PasswordHasher class provides a method to hash a given password using the
 * SHA-256 algorithm and return the hashed password as a hexadecimal string.
 */
public class PasswordHasher {

    /**
     * The `hashPassword` function in Java uses SHA-256 to hash a given password and
     * returns the hashed password as a hexadecimal string.
     * 
     * @param password Thank you for providing the code snippet for hashing a
     *                 password using SHA-256. If you have a specific password that
     *                 you would like to hash, please provide it so that I can
     *                 demonstrate how the `hashPassword` method works with your
     *                 input.
     * @return The `hashPassword` method returns the hashed password as a
     *         hexadecimal string after applying the SHA-256 hashing algorithm to
     *         the input password.
     * @throws RuntimeException
     */
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convert password to bytes and calculate hash
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert hashed bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b); // Convert to unsigned hex
                if (hex.length() == 1) {
                    hexString.append('0'); // Pad single digits with a leading zero
                }
                hexString.append(hex);
            }

            return hexString.toString(); // Return the hashed password as a hex string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not available.", e);
        }
    }
}