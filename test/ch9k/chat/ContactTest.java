/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ContactTest {

    public ContactTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getIp method, of class Contact.
     */
    @Test
    public void testGetIp() {
        System.out.println("getIp");
        Contact instance = null;
        InetAddress expResult = null;
        InetAddress result = instance.getIp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIp method, of class Contact.
     */
    @Test
    public void testSetIp() {
        System.out.println("setIp");
        InetAddress ip = null;
        Contact instance = null;
        instance.setIp(ip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsername method, of class Contact.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        Contact instance = null;
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class Contact.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Contact instance = null;
        String expResult = "";
        String result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class Contact.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "";
        Contact instance = null;
        instance.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isOnline method, of class Contact.
     */
    @Test
    public void testIsOnline() {
        System.out.println("isOnline");
        Contact instance = null;
        boolean expResult = false;
        boolean result = instance.isOnline();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOnline method, of class Contact.
     */
    @Test
    public void testSetOnline() {
        System.out.println("setOnline");
        boolean online = false;
        Contact instance = null;
        instance.setOnline(online);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBlocked method, of class Contact.
     */
    @Test
    public void testIsBlocked() {
        System.out.println("isBlocked");
        Contact instance = null;
        boolean expResult = false;
        boolean result = instance.isBlocked();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBlocked method, of class Contact.
     */
    @Test
    public void testSetBlocked() {
        System.out.println("setBlocked");
        boolean blocked = false;
        Contact instance = null;
        instance.setBlocked(blocked);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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

}