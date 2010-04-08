package ch9k.core;

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


}
