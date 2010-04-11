package ch9k.core;

import ch9k.configuration.PersistentDataObject;
import java.security.MessageDigest;

import ch9k.chat.ContactList;
import ch9k.configuration.Persistable;
import java.util.Arrays;
import org.jdom.Element;

/**
 * Local user info
 * 
 * @author Bruno
 */
public class Account implements Persistable{

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
    private byte[] passwordHash;
    
    
    /**
     * creates a new account in the system.
     * this class only stores a hash of the password
     */
    public Account(String username,String password) {
        this.username = username;
        setPassword(password);
        contactList = new ContactList();
    }
    /**
     * Creates an empty account class, ONLY TO BE USED WHEN LOADING PREVIOUS STATE
     *
     * This creates a completely empty Account object, so that we can load data
     * from a Persistent Data Object we stored previously;
     */
    public Account() {

    }
    /**
     * will return the password hash
     * NOTE: trying to print this is just stupid
     */
    public byte[] getPasswordHash() {
        return passwordHash;
    }
    
    /**
     * method to change the password
     */
    public void setPassword(String password) {
        this.passwordHash = hash(password);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account other = (Account) obj;
        if (!this.username.equals(other.getUsername())) {
            return false;
        }
        if (Arrays.equals(this.passwordHash,other.getPasswordHash())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 67 * hash + Arrays.hashCode(this.passwordHash);
        return hash;
    }


    @Override
    public PersistentDataObject persist() {
        Element pdo = new Element("account");
        pdo.addContent(new Element("username").addContent(username));
        pdo.addContent(new Element("status").addContent(status));
        pdo.addContent(new Element("password").addContent(passwordHash.toString()));
        pdo.addContent(contactList.persist().getElement());

        return new PersistentDataObject(pdo);
    }

    @Override
    public void load(PersistentDataObject object) {
        Element el = object.getElement();
        username = el.getChildText("username");
        status = el.getChildText("status");
        passwordHash = el.getChildText("status").getBytes();
        contactList= new ContactList();
        contactList.load(new PersistentDataObject(el.getChild("contactlist")));
        
    }
}
