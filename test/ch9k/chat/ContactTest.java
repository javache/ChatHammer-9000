package ch9k.chat;

import ch9k.chat.events.ContactEvent;
import ch9k.chat.events.ContactEventFilter;
import ch9k.chat.events.ContactOfflineEvent;
import ch9k.chat.events.ContactOnlineEvent;
import ch9k.chat.events.ContactStatusEvent;
import ch9k.configuration.PersistentDataObject;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TestListener;
import ch9k.network.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jens Panneel
 */
public class ContactTest {
    private Contact contact;
    private InetAddress ip;
    private String username;

    @Before
    public void setUp() throws UnknownHostException {
        username = "JPanneel";
        ip = InetAddress.getByName("google.be");
        contact = new Contact(username, ip, false);
    }

    @After
    public void tearDown() {
        EventPool.getAppPool().clearListeners();
        ChatApplication.getInstance().getAccount().getContactList().clear();
    }

    /**
     * Test of getIp method, of class Contact.
     */
    @Test
    public void testIp() throws UnknownHostException {
        assertEquals(contact.getIp(), ip);

        InetAddress newIp = InetAddress.getByName("ugent.be");
        contact.setIp(newIp);
        assertEquals(newIp, contact.getIp());
    }

    /**
     * Test of getUsername method, of class Contact.
     */
    @Test
    public void testGetUsername() {
        assertEquals(contact.getUsername(), username);
    }

    /**
     * Test of getStatus/setStatus method, of class Contact.
     */
    @Test
    public void testStatus() {
        String status = "Having Great Sex!";
        contact.setStatus(status);
        assertEquals(contact.getStatus(), status);
    }

    /**
     * Test of isOnline/setOnline method, of class Contact.
     */
    @Test
    public void testOnline() {
        assertFalse(contact.isOnline());
        contact.setOnline(true);
        assertTrue(contact.isOnline());
    }

    /**
     * Test of isBlocked/setBlocked method, of class Contact.
     */
    @Test
    public void testBlocked() throws InterruptedException {
        assertFalse(contact.isBlocked());

        TestListener testListener = new TestListener();
        EventPool.getAppPool().addListener(testListener, new EventFilter(ContactEvent.class));

        contact.setBlocked(true);
        Thread.sleep(50);

        assertTrue(testListener.getLastEvent() instanceof ContactOfflineEvent);
        // should tell the blocked contact that you went offline
        assertEquals(contact, ((ContactOfflineEvent)testListener.getLastEvent()).getContact());
        assertTrue(contact.isBlocked());

        contact.setBlocked(false);
        Thread.sleep(50);
        assertTrue(testListener.getLastEvent() instanceof ContactOnlineEvent);
        // should tell the unblocked contact you came online again
        assertEquals(contact, ((ContactOnlineEvent)testListener.getLastEvent()).getContact());
        assertFalse(contact.isBlocked());
    }

    /**
     * Test of equals method, of class Contact.
     */
    @Test
    public void testEquals() throws UnknownHostException {
        Contact instance1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance2 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance3 = new Contact("JPanneel", InetAddress.getByName("ugent.be"), false);
        Contact instance4 = new Contact("Javache", InetAddress.getByName("ugent.be"), false);

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance3.equals(instance4));
    }

    /**
     * Test of hashCode method, of class Contact.
     */
    @Test
    public void testHashCode() throws UnknownHostException {
        Contact instance1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance2 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance3 = new Contact("Javache", InetAddress.getByName("google.be"), false);
        
        assertTrue(instance1.hashCode() == instance2.hashCode());
        assertFalse(instance1.hashCode() == instance3.hashCode());
    }

    /**
     * Test of compareTo method, of class Contact.
     */
    @Test
    public void testCompareTo() throws UnknownHostException {
        Contact contact1 = new Contact("Javache", InetAddress.getByName("google.be"), false);
        Contact contact2 = new Contact("Zeusje", InetAddress.getByName("google.be"), false);
        Contact contact3 = new Contact("Zeusje", InetAddress.getByName("ugent.be"), false);

        // should be consistent with equals
        assertTrue(contact.compareTo(contact) == 0);

        // sgn(compare(x, y)) == -sgn(compare(y, x))
        assertTrue(contact.compareTo(contact1) > 0);
        assertTrue(contact1.compareTo(contact) < 0);

        // sgn(compare(x, y)) == -sgn(compare(y, x))
        assertTrue(contact2.compareTo(contact3) < 0);
        assertTrue(contact3.compareTo(contact2) > 0);

        // transitivity
        assertTrue(contact1.compareTo(contact2) < 0);
        assertTrue(contact2.compareTo(contact3) < 0);
        assertTrue(contact1.compareTo(contact3) < 0);
    }

    /**
     * Test of handelEvent method, of class Contact.
     * only tests the local side
     */
    @Test
    public void testHandleEvent() throws UnknownHostException, InterruptedException {
        EventPool eventPool = EventPool.getAppPool();

        ContactEvent contactOnlineEvent = new ContactOnlineEvent(contact);
        ContactEvent contactOfflineEvent = new ContactOfflineEvent(contact);

        String oldStatus = contact.getStatus();
        String newStatus = "on toilet";
        ContactEvent contactStatusChangeEvent = new ContactStatusEvent(contact, newStatus);

        assertFalse(contact.isOnline());
        assertEquals(oldStatus, contact.getStatus());

        eventPool.raiseEvent(contactOnlineEvent);
        eventPool.raiseEvent(contactStatusChangeEvent);
        Thread.sleep(100);

        assertFalse(contact.isOnline());
        assertEquals(oldStatus, contact.getStatus());

        eventPool.raiseEvent(contactOfflineEvent);
        Thread.sleep(100);

        assertFalse(contact.isOnline());
    }

    /**
     * Test of handelEvent method, of class Contact.
     * only tests the remote side
     * handshake could not be tested, because remotepool cannot broadcast trough network...
     */

    @Test
    public void testRemoteHandleEvent() throws UnknownHostException, InterruptedException, IOException {
        // get the local app-pool and let it start
        EventPool localPool = EventPool.getAppPool();
        Thread.sleep(100);

        // create a remote eventpool and connect it to the local one
        EventPool remotePool = new EventPool();
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool, null);
        Thread.sleep(100);

        // create a contact out of the local user (as seen from the other side)
        Account localAccount = ChatApplication.getInstance().getAccount();
        Contact localContact = new Contact(localAccount.getUsername(), InetAddress.getLocalHost(), false);
        Contact remoteContact = new Contact("Javache", InetAddress.getLocalHost(), false);

        // add both of these contacts to our list, so we can look them up later
        ContactList contactList = localAccount.getContactList();
        contactList.addContact(localContact);
        contactList.addContact(remoteContact);

        // remove the remotecontact from the list of local listeners, because we want to test the remote side
        localPool.removeListener(remoteContact);
        // locally this is not a contact, so not a listener
        localPool.removeListener(localContact);
        // the local contact is a real contact on remote side, so it should listen
        remotePool.addListener(localContact, new ContactEventFilter(localContact));

        // create the events localContact changes status and notifies remotecontact
        ContactEvent contactOnlineEvent = new ContactOnlineEvent(remoteContact);
        ContactEvent contactOfflineEvent = new ContactOfflineEvent(remoteContact);

        String newStatus = "on toilet";
        ContactEvent contactStatusChangeEvent = new ContactStatusEvent(remoteContact, newStatus);

        assertFalse(localContact.isOnline());
        assertFalse(localContact.isBlocked());
        assertEquals("", localContact.getStatus());

        localPool.raiseEvent(contactOnlineEvent);
        Thread.sleep(100);
        assertTrue(localContact.isOnline());

        localPool.raiseEvent(contactStatusChangeEvent);
        Thread.sleep(100);
        assertEquals(newStatus, localContact.getStatus());

        localPool.raiseEvent(contactOfflineEvent);
        Thread.sleep(100);
        assertFalse(localContact.isOnline());
    }

    @Test
    public void testPersist() throws UnknownHostException {
        Contact instance = new Contact("Javache", InetAddress.getByName("ugent.be"), false);
        PersistentDataObject pdo = instance.persist();
        Contact loaded = new Contact(pdo);

        assertEquals(instance, loaded);
    }
}
