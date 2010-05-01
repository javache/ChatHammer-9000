package ch9k.chat;

import ch9k.configuration.PersistentDataObject;
import ch9k.core.ChatApplication;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContactListTest {
    private ContactList contactList;

    @Before
    public void setUp() {
        ChatApplication app = ChatApplication.getInstance();
        app.performTestLogin();
        contactList = new ContactList(app.getAccount());
    }

    /**
     * Test of getContacts method, of class ContactList.
     */
    @Test
    public void testGetContacts() throws UnknownHostException {
        // look out, this is a sorted list ;)
        Contact[] contacts = new Contact[] {
            new Contact("jaspervdj", InetAddress.getByName("4chan.org")),
            new Contact("Javache", InetAddress.getByName("ugent.be")),
            new Contact("JPanneel", InetAddress.getByName("google.be"))
        };

        for(Contact contact : contacts) {
            contactList.addContact(contact);
        }

        assertArrayEquals(contacts, contactList.getContacts().toArray());
    }

    /**
     * Test of addContact method, of class ContactList.
     */
    @Test
    public void testAddContact() throws UnknownHostException  {
        Contact contact = new Contact("JPanneel", InetAddress.getByName("google.be"));
        
        contactList.addContact(contact);
        assertEquals(1, contactList.getContacts().size());
        
        contactList.addContact(contact);
        assertEquals(1, contactList.getContacts().size());
    }

    /**
     * Test of removeContact method, of class ContactList.
     */
    @Test
    public void testRemoveContact() throws UnknownHostException {
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"));
        Contact contact2 = new Contact("JPanneel", InetAddress.getByName("ugent.be"));
        
        assertEquals(0, contactList.getContacts().size());
        contactList.addContact(contact1);
        contactList.removeContact(contact2);
        assertEquals(1, contactList.getContacts().size());
        contactList.removeContact(contact1);
        assertEquals(0, contactList.getContacts().size());
    }

    /**
     * Test of removeContact method, of class ContactList.
     */
    @Test
    public void testGetContact() throws UnknownHostException {
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"));
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"));

        contactList.addContact(contact1);
        contactList.addContact(contact2);

        assertEquals(contact1, contactList.getContact(contact1.getIp(),contact1.getUsername()));
        assertNotSame(contact1, contactList.getContact(contact2.getIp(),contact2.getUsername()));
        assertNull(contactList.getContact(contact2.getIp(),contact1.getUsername()));
        assertNull(contactList.getContact(contact1.getIp(),contact2.getUsername()));
    }

    @Test
    public void testPersist() throws UnknownHostException, InterruptedException{
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"));
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"));
        contactList.addContact(contact1);
        contactList.addContact(contact2);

        PersistentDataObject pdo = contactList.persist();
        ContactList loadedList = new ContactList(ChatApplication.getInstance().getAccount(), pdo);
        
        assertTrue(contactList.getContacts().containsAll(loadedList.getContacts()));
        assertTrue(loadedList.getContacts().containsAll(contactList.getContacts()));
    }
}
