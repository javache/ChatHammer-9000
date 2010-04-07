/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ContactListTest {

    public ContactListTest() {
    }

    /**
     * Test of getContacts method, of class ContactList.
     */
    @Test
    public void testGetContacts() throws UnknownHostException {
        System.out.println("getContacts");
        ContactList contactList = new ContactList(new HashSet<Contact>());
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"), false);
        Contact contact3 = new Contact("jaspervdj", InetAddress.getByName("4chan.org"), false);
        contactList.addContact(contact1);
        contactList.addContact(contact2);
        contactList.addContact(contact3);
        Set<Contact> contacts = contactList.getContacts();
        assertEquals(contacts.size(), 3);
        assertTrue(contacts.contains(contact1));
        assertTrue(contacts.contains(contact2));
        assertTrue(contacts.contains(contact3));
    }

    /**
     * Test of addContact method, of class ContactList.
     */
    @Test
    public void testAddContact() throws UnknownHostException  {
        System.out.println("addContact");
        ContactList contactList = new ContactList(new HashSet<Contact>());
        Contact contact = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
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
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact contact2 = new Contact("JPanneel", InetAddress.getByName("ugent.be"), false);
        contactList.removeContact(contact1);
        assertEquals(0, contactList.getContacts().size());
        contactList.addContact(contact1);
        contactList.removeContact(contact2);
        assertEquals(1, contactList.getContacts().size());
        contactList.removeContact(contact1);
        assertEquals(0, contactList.getContacts().size());
    }

}