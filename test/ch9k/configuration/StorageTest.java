/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.configuration;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.core.Account;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Bruno
 */
public class StorageTest {
    
    private Storage storage;
    private Account account;

    @Before
    public void setUp(){
        storage = new Storage("Bruno");
        account = new Account("Bruno", "test");
        storage.store("account", account);
        try {
            ContactList contacts = account.getContactList();
            contacts.addContact(new Contact("Zeus WPI", InetAddress.getByName("google.be"), false));
            contacts.addContact(new Contact("CH9K", InetAddress.getByName("10.1.1.70"), false));
        } catch (UnknownHostException ex) {
            Logger.getLogger(StorageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        storage.save();
    }


    @Test
    public void loadTest(){
        Storage newstorage = new Storage("Bruno");
        Account bruno = new Account(newstorage.fetch("account"));
        assertEquals(bruno, account);
    }

}
