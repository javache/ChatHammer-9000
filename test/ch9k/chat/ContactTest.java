package ch9k.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
    public void testBlocked() {
        assertFalse(contact.isBlocked());
        contact.setBlocked(true);
        assertTrue(contact.isBlocked());
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
        assertTrue(contact2.compareTo(contact3) > 0);
        assertTrue(contact3.compareTo(contact2) < 0);

        // transitivity
        assertTrue(contact1.compareTo(contact2) > 0);
        assertTrue(contact2.compareTo(contact3) > 0);
        assertTrue(contact1.compareTo(contact3) > 0);
    }
}
