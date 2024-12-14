package myexpense.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // Length of salt in bytes

    /**
     * The `hashPassword` function hashes a password with a random salt and returns
     * both the salt and hashed password as a string.
     * 
     * @param password The password to be hashed.
     * @return The hashed password combined with the salt, encoded in Base64.
     */
    public static String hashPassword(String password) {
        try {
            // Generate a new random salt
            byte[] salt = generateSalt();

            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            // Add the salt to the password
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes());

            // Combine the salt and the hashed password, and encode them in Base64
            byte[] combined = new byte[salt.length + hashedBytes.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedBytes, 0, combined, salt.length, hashedBytes.length);

            return Base64.getEncoder().encodeToString(combined); // Return the combined salt and hash as Base64
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not available.", e);
        }
    }

    /**
     * The `verifyPassword` function checks if a provided password matches the
     * stored
     * hashed password.
     * 
     * @param password   The password to verify.
     * @param storedHash The stored hashed password with the salt.
     * @return True if the password matches the stored hash, false otherwise.
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Decode the stored hash from Base64
            byte[] storedHashBytes = Base64.getDecoder().decode(storedHash);

            // Extract the salt from the stored hash
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(storedHashBytes, 0, salt, 0, SALT_LENGTH);

            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            // Add the salt to the password
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes());

            // Compare the hashes
            for (int i = 0; i < hashedBytes.length; i++) {
                if (storedHashBytes[SALT_LENGTH + i] != hashedBytes[i]) {
                    return false; // Password does not match
                }
            }

            return true; // Password matches
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not available.", e);
        }
    }

    /**
     * Generates a random salt using SecureRandom.
     * 
     * @return A byte array representing the generated salt.
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt); // Fill the byte array with random bytes
        return salt;
    }
}
