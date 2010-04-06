/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
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
public class ContactListTest {

    public ContactListTest() {
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
     * Test of getContacts method, of class ContactList.
     */
    @Test
    public void testGetContacts() {
        System.out.println("getContacts");
        ContactList instance = null;
        Set expResult = null;
        Set result = instance.getContacts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addContact method, of class ContactList.
     */
    @Test
    public void testAddContact() throws UnknownHostException  {
        System.out.println("addContact");
        ContactList contactList = new ContactList(new HashSet<Contact>());
        Contact contact = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
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
        System.out.println("removeContact");
        ContactList contactList = new ContactList(new HashSet<Contact>());
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        Contact contact2 = new Contact("JPanneel", InetAddress.getByName("ugent.be"), true);
        contactList.removeContact(contact1);
        assertEquals(0, contactList.getContacts().size());
        contactList.addContact(contact1);
        contactList.removeContact(contact2);
        assertEquals(1, contactList.getContacts().size());
        contactList.removeContact(contact1);
        assertEquals(0, contactList.getContacts().size());
    }

}