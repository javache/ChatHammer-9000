package ch9k.chat;

import ch9k.chat.event.ContactOfflineEvent;
import ch9k.chat.event.ContactOnlineEvent;
import ch9k.chat.event.ContactRequestEvent;
import ch9k.chat.event.ContactStatusEvent;
import ch9k.configuration.Persistable;
import ch9k.configuration.PersistentDataObject;
import ch9k.core.Account;
import ch9k.core.I18n;
import ch9k.core.event.AccountEvent;
import ch9k.core.event.AccountLoginEvent;
import ch9k.core.event.AccountLogoffEvent;
import ch9k.core.event.AccountStatusEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.Connection;
import ch9k.network.event.UserDisconnectedEvent;
import java.awt.EventQueue;
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
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * List of al the contacts of the current user.
 * @author Jens Panneel
 */
public class ContactList extends AbstractListModel 
        implements Persistable, ChangeListener, EventListener {
    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(ContactList.class);

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
     * should we ping?
     */
    private boolean shouldPing = false;

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
        EventPool pool = EventPool.getAppPool();
        pool.addListener(this, new EventFilter(AccountEvent.class));

        listeners.add(new ContactOnlineListener());
        pool.addListener(listeners.get(0),
                new EventFilter(ContactOnlineEvent.class));

        listeners.add(new ContactOfflineListener());
        pool.addListener(listeners.get(1),
                new EventFilter(ContactOfflineEvent.class));

        listeners.add(new ContactStatusListener());
        pool.addListener(listeners.get(2),
                new EventFilter(ContactStatusEvent.class));

        listeners.add(new ContactRequestListener());
        pool.addListener(listeners.get(3),
                new EventFilter(ContactRequestEvent.class));

        listeners.add(new UserDisconnectedListener());
        pool.addListener(listeners.get(4),
                new EventFilter(UserDisconnectedEvent.class));

        if(!Connection.TRUST_TCP) {
            new PingContactThread().start();
            shouldPing = true;
        }
    }

    @Override
    public void handleEvent(Event event) {
        if(event instanceof AccountLoginEvent) {
            broadcastOnline();
        }

        if(event instanceof AccountLogoffEvent) {
            shouldPing = false;
            broadcastOffline();
            
            EventPool pool = EventPool.getAppPool();
            for(EventListener listener : listeners) {
                pool.removeListener(listener);
            }
            pool.removeListener(this);
        }

        if(event instanceof AccountStatusEvent) {
            AccountStatusEvent accountEvent = (AccountStatusEvent)event;
            EventPool pool = EventPool.getAppPool();
            for(Contact contact : onlineHash.values()) {
                pool.raiseNetworkEvent(new ContactStatusEvent(contact,
                        accountEvent.getStatus()));
            }
        }
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
                if(onlineEvent.requiresReponse() || !contact.isOnline()) {
                    EventPool.getAppPool().raiseNetworkEvent(new ContactOnlineEvent(contact));
                    
                    /* keep in mind here that this will set the state to online
                     * so in essence this will also handle responses
                     * from friendrequests
                     */
                    contact.setOnline(true);
                    contact.setStatus(onlineEvent.getStatus());
                    onlineHash.put(onlineEvent.getSource(), contact);

                    fireListChanged();
                }
            }
        }
    }
    
    private class ContactOfflineListener implements EventListener {
        @Override
        public void handleEvent(Event event) {
            ContactOfflineEvent offlineEvent = (ContactOfflineEvent)event;
            Contact contact = offlineEvent.getContact();
            /* all this has to be true, otherwise we just have to ignore */
            if(contact != null  && !contact.isIgnored() && !contact.isBlocked()) {
                contact.setOnline(false);
                onlineHash.remove(offlineEvent.getSource());
            }
        }
    }

    private class ContactStatusListener implements EventListener {
        @Override
        public void handleEvent(Event event) {
            ContactStatusEvent statusEvent = (ContactStatusEvent)event;
            Contact contact = statusEvent.getContact();
            /* all this has to be true, otherwise we just have to ignore */
            if(contact != null && statusEvent.isExternal()) {
                contact.setStatus(statusEvent.getNewStatus());
            }
        }
    }


    private class UserDisconnectedListener implements EventListener {
        @Override
        public void handleEvent(Event ev) {
            UserDisconnectedEvent event = (UserDisconnectedEvent)ev;
            Contact contact = onlineHash.get(event.getSource());
            if(contact != null) {
                NetworkEvent contactOfflineEvent = new ContactOfflineEvent(contact);
                contactOfflineEvent.setSource(event.getSource());
                EventPool.getAppPool().raiseEvent(contactOfflineEvent);
            }
        }
    }

    private class ContactRequestListener implements EventListener {
        @Override
        public void handleEvent(Event ev) {
            final ContactRequestEvent event = (ContactRequestEvent)ev;
            if(event.isExternal() && event.getUsername().equalsIgnoreCase(account.getUsername())) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        /* first we see if we already have the contact in our list */
                        Contact contact = getContact(event.getSource(), event.getRequester());
                        if (contact != null) {
                            /* we return because we already declined this request */
                            return;
                        }
                        // WTF is this doing here
                        // last time I checked ContactList wasn't a view.
                        String text = I18n.get("ch9k.chat", "add_friend", event.getRequester());
                        int confirmation = JOptionPane.showConfirmDialog(
                                null, text, I18n.get("ch9k.chat", "add_friend_title"), JOptionPane.YES_NO_OPTION);

                        contact = new Contact(event.getRequester(), event.getSource());
                        if (confirmation != JOptionPane.YES_OPTION) {
                            contact.setIgnored();
                        }
                        addContact(contact, false);
                        pingContact(contact);
                    }
                });
            }
        }
    }

    private class PingContactThread extends Thread {
        private int timeout = 15000;

        public PingContactThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(shouldPing) {
                try {
                    Thread.sleep(timeout);
                    for(Contact contact : contacts) {
                        pingContact(contact);
                    }
                } catch(InterruptedException ex) {
                    
                }
            }
        }
    }

    private void pingContact(Contact contact) {
        logger.info("Pinging contact " + contact.getUsername());
        if (contact.isRequested()) {
            EventPool.getAppPool().raiseNetworkEvent(new ContactRequestEvent(
                    contact.getIp(), contact.getUsername(), account.getUsername()));
        } else if(!contact.isIgnored()) {
            EventPool.getAppPool().raiseNetworkEvent(new ContactOnlineEvent(contact, true));
        }
    }
    
    /**
     * Add a contact
     * @param contact
     * @param sendRequest send a friendrequest?
     */
    public void addContact(Contact contact, boolean sendRequest) {
        boolean success = contacts.add(contact);
        
        if(success) {
            logger.info("Added contact " + contact.getUsername());

            if(sendRequest) {
                contact.setRequested();
                pingContact(contact);
            }
            contact.addChangeListener(this);
            fireListChanged();
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
        logger.info("Removing contact " + contact.getUsername());

        if(contact.isOnline()) {
            EventPool.getAppPool().raiseNetworkEvent(new ContactOfflineEvent(contact));
            onlineHash.remove(contact.getIp());
        }
        contacts.remove(contact);
        fireListChanged();
    }

    /**
     * Sends all contacts that the current account is now online
     */
    private void broadcastOnline() {
        for(Contact contact : contacts) {
            pingContact(contact);
        }
    }
    
    /**
     * Send all contacts that the current account is now offline
     */
    private void broadcastOffline() {
        for(Contact contact : onlineHash.values()) {
            EventPool.getAppPool().raiseNetworkEvent(new ContactOfflineEvent(contact));
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
                contact.getUsername().equalsIgnoreCase(username)){
            return contact;
        }
        
        for (Contact cont : contacts) {
            if(cont.getIp().equals(ip) && cont.getUsername().equalsIgnoreCase(username)) {
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
            addContact(new Contact(new PersistentDataObject(child)), false);
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
