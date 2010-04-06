/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

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
public class ConversationManagerTest {

    public ConversationManagerTest() {
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
     * Test of startConversation method, of class ConversationManager.
     */
    @Test
    public void testStartConversation() {
        System.out.println("startConversation");
        Contact contact = null;
        ConversationManager instance = new ConversationManager();
        Conversation expResult = null;
        Conversation result = instance.startConversation(contact);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeConversation method, of class ConversationManager.
     */
    @Test
    public void testCloseConversation() {
        System.out.println("closeConversation");
        Contact contact = null;
        ConversationManager instance = new ConversationManager();
        instance.closeConversation(contact);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConversation method, of class ConversationManager.
     */
    @Test
    public void testGetConversation() {
        System.out.println("getConversation");
        Contact contact = null;
        ConversationManager instance = new ConversationManager();
        Conversation expResult = null;
        Conversation result = instance.getConversation(contact);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}