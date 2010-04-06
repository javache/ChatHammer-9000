/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

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
    public void testAddContact() throws Exception {
        System.out.println("addContact");
        Contact contact = null;
        ContactList instance = null;
        instance.addContact(contact);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeContact method, of class ContactList.
     */
    @Test
    public void testRemoveContact() {
        System.out.println("removeContact");
        Contact contact = null;
        ContactList instance = null;
        instance.removeContact(contact);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}