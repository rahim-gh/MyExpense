/**
 * @author rahim
 */
package myexpense.models;

/**
 * The `Account` class represents a user account with properties such as
 * accountId, email, username, and passwordHash.
 */
public class Account {
    private String email;
    private String username;
    private String passwordHash;

    public Account() {
        setEmail(email);
        setUsername(username);
        setPasswordHash(passwordHash);
    }

    /**
     * The getEmail function returns the email address.
     * 
     * @return Returning the email address stored in the variable "email".
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setEmail function sets the email address for an object.
     * 
     * @param email The `email` parameter is the email address that will be assigned
     *              to the object's `email` attribute.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The getUsername function returns the username.
     * 
     * @return Returning the value of the variable `username`.
     */
    public String getUsername() {
        return username;
    }

    /**
     * The function sets the username for an object.
     * 
     * @param username The `username` parameter is the value that will be assigned
     *                 to the `username` instance variable.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The getPasswordHash function returns the password hash.
     * 
     * @return Returning the `passwordHash` string.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * The function sets the password hash for a given object.
     * 
     * @param passwordHash The `passwordHash` parameter is the hashed value of the
     *                     user's password that will be stored in the system for
     *                     authentication purposes.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
