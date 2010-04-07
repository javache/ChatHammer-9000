/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ContactTest {
    private Contact contact;
    private InetAddress ip;
    private String username;

    public ContactTest() {
    }

    @Before
    public void setUp() throws UnknownHostException {
        username = "JPanneel";
        ip = InetAddress.getByName("google.be");
        contact = new Contact(username, ip, false);
    }

    @After
    public void tearDown() {
        contact = null;
    }

    /**
     * Test of getIp method, of class Contact.
     */
    @Test
    public void testGetIp() {
        System.out.println("getIp");
        assertEquals(contact.getIp(), ip);
    }

    /**
     * Test of setIp method, of class Contact.
     */
    @Test
    public void testSetIp() throws UnknownHostException {
        System.out.println("setIp");
        InetAddress ip2 = InetAddress.getByName("ugent.be");
        contact.setIp(ip2);
        assertEquals(ip2, contact.getIp());
    }

    /**
     * Test of getUsername method, of class Contact.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        assertEquals(contact.getUsername(), username);
    }

    /**
     * Test of getStatus method, of class Contact.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        String status = "Having Great Sex!";
        contact.setStatus(status);
        assertEquals(contact.getStatus(), status);
    }

    /**
     * Test of setStatus method, of class Contact.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "Having Great Sex!";
        contact.setStatus(status);
        assertEquals(status, contact.getStatus());
    }

    /**
     * Test of isOnline method, of class Contact.
     */
    @Test
    public void testIsOnline() {
        System.out.println("isOnline");
        assertFalse(contact.isOnline());
        contact.setOnline(true);
        assertTrue(contact.isOnline());
    }

    /**
     * Test of setOnline method, of class Contact.
     */
    @Test
    public void testSetOnline() {
        System.out.println("setOnline");
        assertFalse(contact.isOnline());
        contact.setOnline(true);
        assertTrue(contact.isOnline());
    }

    /**
     * Test of isBlocked method, of class Contact.
     */
    @Test
    public void testIsBlocked() {
        System.out.println("isBlocked");
        assertFalse(contact.isBlocked());
        contact.setBlocked(true);
        assertTrue(contact.isBlocked());
    }

    /**
     * Test of setBlocked method, of class Contact.
     */
    @Test
    public void testSetBlocked() {
        System.out.println("setBlocked");
        assertFalse(contact.isBlocked());
        contact.setBlocked(true);
        assertTrue(contact.isBlocked());
    }

    /**
     * Test of equals method, of class Contact.
     */
    @Test
    public void testEquals() throws UnknownHostException {
        System.out.println("equals");
        Contact instance1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance2 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        assertTrue(instance1.equals(instance2));
        Contact instance3 = new Contact("JPanneel", InetAddress.getByName("ugent.be"), false);
        assertFalse(instance1.equals(instance3));
        Contact instance4 = new Contact("Javache", InetAddress.getByName("ugent.be"), false);
        assertFalse(instance3.equals(instance4));
    }

    /**
     * Test of hashCode method, of class Contact.
     */
    @Test
    public void testHashCode() throws UnknownHostException {
        System.out.println("hashCode");
        Contact instance1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact instance2 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        assertEquals(instance1.hashCode(), instance2.hashCode());
        Contact instance3 = new Contact("Javache", InetAddress.getByName("google.be"), false);
        assertNotSame(instance1.hashCode(), instance3.hashCode());
    }

    /**
     * Test of compareTo method, of class Contact.
     */
    @Test
    public void testCompareTo() throws UnknownHostException {
        System.out.println("compareTo");
        assertTrue(contact.compareTo(contact) == 0);
        Contact contact2 = new Contact("Javache", InetAddress.getByName("google.be"), false);
        assertTrue(contact.compareTo(contact2) > 0);
        Contact contact3 = new Contact("Zeusje", InetAddress.getByName("google.be"), false);
        assertTrue(contact.compareTo(contact3) < 0);
        Contact contact4 = new Contact("Zeusje", InetAddress.getByName("ugent.be"), false);
        assertTrue(contact3.compareTo(contact4) > 0);
    }

}