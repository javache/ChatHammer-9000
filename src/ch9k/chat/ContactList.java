package ch9k.chat;

import ch9k.chat.event.ContactOfflineEvent;
import ch9k.chat.event.ContactOnlineEvent;
import ch9k.chat.event.ContactRequestEvent;
import ch9k.configuration.Persistable;
import ch9k.configuration.PersistentDataObject;
import ch9k.core.Account;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
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
     * Reference to the account this list belongs to
     */
    private Account account;

    /**
     * EventListeners managed by this class
     */
    private List<EventListener> listeners;

    /**
     * Construct a new ContactList
     * @param account
     */
    public ContactList(Account account) {
        contacts = new TreeSet<Contact>();
        onlineHash = new HashMap<InetAddress,Contact>();
        listeners = new ArrayList<EventListener>();
        this.account = account;
        init();
    }
    
    /**
     * Construct a new ContactList, and immediately restores it to a previous state
     * @param account
     * @param data Previously stored state of this object
     */
    public ContactList(Account account, PersistentDataObject data) {
        this(account);
        load(data);
    }

    private void init() {
        listeners.add(new ContactOnlineListener());
        EventPool.getAppPool().addListener(listeners.get(0),
                new EventFilter(ContactOnlineEvent.class));

        listeners.add(new ContactOfflineListener());
        EventPool.getAppPool().addListener(listeners.get(1),
                new EventFilter(ContactOfflineEvent.class));

        listeners.add(new ContactRequestListener());
        EventPool.getAppPool().addListener(listeners.get(2),
                new EventFilter(ContactRequestEvent.class));
    }

    /**
     * Get a list of all the contacts from the current user
     * @return contacts
     */
    public SortedSet<Contact> getContacts() {
        return contacts;
    }
    
    private class ContactOnlineListener implements EventListener {
        @Override
        public void handleEvent(Event event) {
            ContactOnlineEvent onlineEvent = (ContactOnlineEvent)event;
            Contact contact = onlineEvent.getContact();
            /* all this has to be true, otherwise we just have to ignore */
            if(onlineEvent.isExternal() && contact != null &&
                    !contact.isIgnored() && !contact.isBlocked()) {
                if(!contact.isOnline()) {
                    EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(contact));
                }
                
                /* keep in mind here that this will set the state to online
                 * so in essence this will also handle responses 
                 * from friendrequests 
                 */
                contact.setOnline(true);
                onlineHash.put(onlineEvent.getSource(), contact);

                fireListChanged();    
            }
        }
    }
    
    private class ContactOfflineListener implements EventListener {
        @Override
        public void handleEvent(Event event) {
            ContactOfflineEvent offlineEvent = (ContactOfflineEvent)event;
            Contact contact = offlineEvent.getContact();
            /* all this has to be true, otherwise we just have to ignore */
            if(offlineEvent.isExternal() && contact != null  &&
                    !contact.isIgnored() && !contact.isBlocked()) {
                contact.setOnline(false);
                onlineHash.remove(offlineEvent.getSource());
            }
        }
    }
    
    private class ContactRequestListener implements EventListener {
        @Override
        public void handleEvent(Event ev) {
            ContactRequestEvent event = (ContactRequestEvent)ev;
            if(event.isExternal() && event.getUsername().equals(account.getUsername())) {
                String text = "would you like to add " + event.getRequester()
                        + " as your friend?";
                int confirmation = JOptionPane.showConfirmDialog(
                        null, text, "Friend Request", JOptionPane.YES_NO_OPTION);

                Contact contact = new Contact(event.getRequester(),event.getSource());
                if (confirmation != JOptionPane.YES_OPTION) {
                    contact.setIgnored();
                }
                addContact(contact, false);
            }
        }
    }
    
    private void pingContact(Contact contact) {
        if (contact.isRequested()) {
            EventPool.getAppPool().raiseEvent(new ContactRequestEvent(
                    contact.getIp(), contact.getUsername(), account.getUsername()));
        } else {
            EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(contact));            
        }

    }
    
    /**
     * Add a contact
     * @param contact
     * @param sendRequest send a friendrequest?
     */
    public void addContact(Contact contact, boolean sendRequest) {
        boolean success = contacts.add(contact);
        if(sendRequest) {
            contact.setRequested();
        }

        if(success) {
            contact.addChangeListener(this);
            fireListChanged();
            pingContact(contact);
        }
    }
    
    /**
     * Add a contact and send a friendrequest
     * @param contact
     */
    public void addContact(Contact contact) {
        addContact(contact, false);
    }
    
    /**
     * Remove the given contact
     * @param contact
     */
    public void removeContact(Contact contact) {
        contacts.remove(contact);
        if(contact.isOnline()) {
            onlineHash.remove(contact.getIp());
        }
        fireListChanged();
    }
    
    /**
     * Send all contacts that the current account is offline now
     * and remove any listeners
     */
    public void broadcastOffline() {
        EventPool pool = EventPool.getAppPool();
        for(EventListener listener : listeners) {
            pool.removeListener(listener);
        }

        for(Contact contact : contacts) {
            if(contact.isOnline()) {
                EventPool.getAppPool().raiseEvent(new ContactOfflineEvent(contact));
            }
        }
    }

    /**
     * Get the contact that is know by this info, returns null if no such contact is found
     * will first look in it's online hash, since lookup in there is much easier and faster
     * if not found there, search in all the contacts
     * @param ip
     * @param username
     * @return contact
     */
    public Contact getContact(InetAddress ip, String username) {
        Contact contact = onlineHash.get(ip);
        if(contact != null && 
                contact.getIp().equals(ip) &&
                contact.getUsername().equals(username)){
            return contact;
        }
        
        for (Contact cont : contacts) {
            if(cont.getIp().equals(ip) && cont.getUsername().equals(username)) {
                return cont;
            }
        }
        
        return null;
    }
    
    private void fireListChanged() {
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
    public Object getElementAt(int j) {
        if(j >= contacts.size()) {
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

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        if(changeEvent.getSource() instanceof Contact) {
            Contact contact = (Contact)changeEvent.getSource();
            fireListChanged();
        }
    }
}
