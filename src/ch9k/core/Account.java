package ch9k.core;

import java.security.MessageDigest;

import ch9k.chat.ContactList;

/**
 * Local user info
 * 
 * @author Bruno
 */
public class Account {

    /**
     * The users contactlist
     */
    private ContactList contactList;

    /**
     * The users name within the network
     */
    private String username;

    /**
     * Users current status
     */
    private String status;
    
    /**
     * the hash of the password
     */
    private byte[] hash;
    
    
    /**
     * creates a new account in the system.
     * this class only stores a hash of the password
     */
    public Account(String username,String password) {
        this.username = username;
        this.hash = hash(password);
    }


    /**
     * Getter for the users current contactlist
     * @return Current ContactList
     */
    public ContactList getContactList() {
        return contactList;
    }

    /**
     * Get current personal status
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get username
     * @return Username of the current user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set a new personal status
     * @param status The new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private byte[] hash(String password) {
        try {
            return MessageDigest.getInstance("sha1").digest(password.getBytes());
        } catch(java.security.NoSuchAlgorithmException e) {
            // throw new VeerleFackException
            return null;
        }
    }
}
