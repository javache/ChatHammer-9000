package ch9k.chat;

import java.util.Set;
import java.util.TreeSet;

/**
 * List of al the contacts of the current user.
 * @author Jens Panneel
 */
public class ContactList {
    private Set<Contact> contacts;

    /**
     * Constructor
     * @param contacts The set of all the by configuration known contacts
     *
     * contacts is a Set because you dont want to save the same contact two times, and in saving/loading there is no need for an ordening.
     * In the ContactList there is need for an ordening. This could be done here.
     */
    public ContactList(Set<Contact> contacts) {
        // TODO some sort of sorting/ordening
        this.contacts = new TreeSet<Contact>(contacts);
    }

    /**
     * Get a list of all the contacts from the current user
     * @return contacts
     */
    public Set<Contact> getContacts() {
        return contacts;
    }

    //throws contact already in list exception or returns a boolean
    /**
     * Add a contact to the ContactList.
     * @param contact
     * @throws Exception
     *
     */
    public void addContact(Contact contact) {
        if(!contacts.add(contact)) {
            //TODO throw the right exception
        }
    }

    //throws contact not in list exception or returns a boolean or just dont check. Will only be called on excisting contacts...
    /**
     * Remove the given contact from the ContactList
     * @param contact
     */
    public void removeContact(Contact contact) {
        if(!contacts.remove(contact)) {
            //TODE throw the right exception
        }
    }

}
