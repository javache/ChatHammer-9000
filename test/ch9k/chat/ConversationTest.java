/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import ch9k.plugins.Plugin;
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
public class ConversationTest {

    public ConversationTest() {
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
     * Test of initatedByMe method, of class Conversation.
     */
    @Test
    public void testInitatedByMe() {
        System.out.println("initatedByMe");
        Conversation instance = null;
        boolean expResult = false;
        boolean result = instance.initatedByMe();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContact method, of class Conversation.
     */
    @Test
    public void testGetContact() {
        System.out.println("getContact");
        Conversation instance = null;
        Contact expResult = null;
        Contact result = instance.getContact();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubject method, of class Conversation.
     */
    @Test
    public void testGetSubject() {
        System.out.println("getSubject");
        Conversation instance = null;
        ConversationSubject expResult = null;
        ConversationSubject result = instance.getSubject();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSubject method, of class Conversation.
     */
    @Test
    public void testSetSubject() {
        System.out.println("setSubject");
        ConversationSubject subject = null;
        Conversation instance = null;
        instance.setSubject(subject);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getActivePlugins method, of class Conversation.
     */
    @Test
    public void testGetActivePlugins() {
        System.out.println("getActivePlugins");
        Conversation instance = null;
        Set expResult = null;
        Set result = instance.getActivePlugins();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deletePlugin method, of class Conversation.
     */
    @Test
    public void testDeletePlugin() {
        System.out.println("deletePlugin");
        Class<? extends Plugin> plugin = null;
        Conversation instance = null;
        instance.deletePlugin(plugin);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPlugin method, of class Conversation.
     */
    @Test
    public void testAddPlugin() throws Exception {
        System.out.println("addPlugin");
        Class<? extends Plugin> plugin = null;
        Conversation instance = null;
        instance.addPlugin(plugin);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class Conversation.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        Conversation instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}