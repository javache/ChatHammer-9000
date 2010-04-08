package ch9k.chat;

import java.util.Set;
import java.util.TreeSet;

/**
 * List of al the contacts of the current user.
 * @author Jens Panneel
 */
public class ContactList {
    /**
     * Collection of contacts, a set because you dont want to save
     * the same contact two times.
     */
    private Set<Contact> contacts;

    /**
     * Constructor
     */
    public ContactList() {
        contacts = new TreeSet<Contact>();
    }

    /**
     * Get a list of all the contacts from the current user
     * @return contacts
     */
    public Set<Contact> getContacts() {
        return contacts;
    }

    /**
     * Add a contact to the ContactList.
     * @param contact
     * @return added
     */
    public boolean addContact(Contact contact) {
        return contacts.add(contact);
    }

    /**
     * Remove the given contact from the ContactList
     * @param contact
     * @return removed
     */
    public boolean removeContact(Contact contact) {
        return contacts.remove(contact);
    }
}
