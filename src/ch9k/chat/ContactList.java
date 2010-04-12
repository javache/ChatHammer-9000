package ch9k.chat;

import ch9k.configuration.Persistable;
import ch9k.configuration.PersistentDataObject;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.jdom.Element;

/**
 * List of al the contacts of the current user.
 * @author Jens Panneel
 */
public class ContactList implements Persistable {
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
     * Creates a new object, and immediately restores it to a previous state
     * 
     * @param data Previously stored state of this object
     */
    public ContactList(PersistentDataObject data) {
        contacts = new TreeSet<Contact>();
        load(data);
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

    /**
     * Debug method: remove all contacts
     */
    public void clear() {
        contacts.clear();
    }

    /**
     * Get the contact that is know by this info, returns null if no such contact is found
     * @param ip
     * @param username
     * @return contact
     */
    public Contact getContact(InetAddress ip, String username) {
        Iterator<Contact> it = contacts.iterator();
        Contact contact;
        while(it.hasNext()){
            contact = it.next();
            if(contact.getIp().equals(ip) && contact.getUsername().equals(username)){
                return contact;
            }
        }
        return null;
    }
    

    @Override
    public PersistentDataObject persist() {
        Element contactlist = new Element("contactlist");
        Iterator<Contact> it = contacts.iterator();
        Contact contact;
        while(it.hasNext()){
            contact = it.next();
            contactlist.addContent(contact.persist().getElement());
        }
        return new PersistentDataObject(contactlist);
    }

    @Override
    public void load(PersistentDataObject object) {
        for (Object obj : object.getElement().getChildren()) {
            Element child = (Element) obj;
            Contact contact = new Contact(new PersistentDataObject(child));
            contacts.add(contact);
        }
    }
}
