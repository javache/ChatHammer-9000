package ch9k.chat;

import ch9k.configuration.Persistable;
import ch9k.configuration.PersistentDataObject;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.chat.events.ContactOnlineEvent;
import ch9k.chat.events.ContactOfflineEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.SortedSet;
import javax.swing.AbstractListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom.Element;

/**
 * List of al the contacts of the current user.
 * @author Jens Panneel
 */
public class ContactList extends AbstractListModel implements Persistable, ChangeListener {
    
    /**
     * Collection of contacts, a set because you dont want to save
     * the same contact two times.
     */
    private SortedSet<Contact> contacts;
    
    /**
     * A hashmap because this will make lookup much easier
     * BEWARE: this hash will only contain online contacts
     */
    private HashMap<InetAddress,Contact> onlineHash;

    /**
     * Constructor
     */
    public ContactList() {
        contacts = new TreeSet<Contact>();
        onlineHash = new HashMap<InetAddress,Contact>();
        init();
    }
    
    /**
     * Creates a new object, and immediately restores it to a previous state
     * 
     * @param data Previously stored state of this object
     */
    public ContactList(PersistentDataObject data) {
        contacts = new TreeSet<Contact>();
        onlineHash = new HashMap<InetAddress,Contact>();
        load(data);
        init();
    }

    private void init() {
        EventPool.getAppPool().addListener(new ContactOnlineListener(),
                new EventFilter(ContactOnlineEvent.class));
        EventPool.getAppPool().addListener(new ContactOfflineListener(),
                new EventFilter(ContactOfflineEvent.class));
        pingContacts();
    }

    /**
     * Get a list of all the contacts from the current user
     * @return contacts
     */
    public SortedSet<Contact> getContacts() {
        return contacts;
    }
    
    private void pingContacts() {
        for (int i = 0; i < contacts.size(); i++ ) {
            pingContact(getContactAtIndex(i));
        }
    }
    
    private class ContactOnlineListener implements EventListener {
        public void handleEvent(Event ev) {
            ContactOnlineEvent event = (ContactOnlineEvent)ev;
            Contact contact = event.getContact();
            if(event.isExternal() && contact != null) {
                
                /* when the contact wasn't online, we respond by saying we are online*/
                if (!event.getContact().isOnline()) {
                    EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(event.getContact()));
                }
                if (! contact.isOnline()) {
                    EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(contact));
                }
                contact.setOnline(true);
                onlineHash.put(event.getSource(),contact);
            }
        }
    }
    
    private class ContactOfflineListener implements EventListener {
        public void handleEvent(Event ev) {
            ContactOfflineEvent event = (ContactOfflineEvent)ev;
            event.getContact().setOnline(false);
            onlineHash.remove(event.getSource());
        }
    }
    
    private void pingContact(Contact contact) {
        EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(contact));
    }
    
    /**
     * Add a contact to the ContactList.
     * @param contact
     * @return added
     */
    public void addContact(Contact contact) {
        contact.addChangeListener(this);
        contacts.add(contact);
        updateList();
    }

    /**
     * Remove the given contact from the ContactList
     * @param contact
     * @return removed
     */
    public void removeContact(Contact contact) {
        contacts.remove(contact);
        updateList();
    }
    
    /**
     * convenience, since we often need to access at certain indices
     */
    private Contact getContactAtIndex(int j) {
        if( j >= contacts.size()) {
            return null;
        }
        Iterator<Contact> it = contacts.iterator();
        Contact contact = it.next();
        int i = 0;
        while(it.hasNext() && i < j) {
            contact = it.next();
            i++;
        }
        return contact;
    }
    
    /**
     * Debug method: remove all contacts
     */
    public void clear() {
        contacts.clear();
        updateList();
    }

    /**
     * Get the contact that is know by this info, returns null if no such contact is found
     * @param ip
     * @param username
     * @return contact
     */
    public Contact getContact(InetAddress ip, String username) {
        Contact contact = onlineHash.get(ip);
        if(contact.getIp().equals(ip) && contact.getUsername().equals(username)){
            return contact;
        }
        return null;
    }
    
    private void updateList() {
        fireContentsChanged(this, 0, contacts.size());
    }


    @Override
    public PersistentDataObject persist() {
        Element contactlist = new Element("contactlist");
        for(Contact contact : contacts) {
            contactlist.addContent(contact.persist().getElement());
        }
        return new PersistentDataObject(contactlist);
    }

    @Override
    public void load(PersistentDataObject object) {
        for (Object obj : object.getElement().getChildren()) {
            Element child = (Element) obj;
            Contact contact = new Contact(new PersistentDataObject(child));
            addContact(contact);
        }
    }

    @Override
    public int getSize() {
        return contacts.size();
    }

    @Override
    public Object getElementAt(int n) {
        return getContactAtIndex(n);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        if(changeEvent.getSource() instanceof Contact) {
            Contact contact = (Contact)changeEvent.getSource();
            updateList();
        }
    }
}
