package ch9k.chat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationManagerTest {

    private ConversationManager conversationManager;

    public ConversationManagerTest() {
    }

    @Before
    public void setUp() {
        conversationManager = new ConversationManager();
    }

    @After
    public void tearDown() {
        conversationManager = null;
    }

    /**
     * Test of startConversation method, of class ConversationManager.
     */
    @Test
    public void testStartConversation() {
        System.out.println("startConversation");
        Contact contact = new Contact("Javache", null, true);
        Conversation conversation = new Conversation(contact, true);
        assertEquals(conversation, conversationManager.startConversation(contact));
        assertEquals(conversation, conversationManager.getConversation(contact));
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
        fail("Close() is yet to be implemented");
    }

    /**
     * Test of getConversation method, of class ConversationManager.
     */
    @Test
    public void testGetConversation() {
        System.out.println("getConversation");
        Contact contact = new Contact("Javache", null, true);
        Conversation conversation = new Conversation(contact, true);
        assertEquals(conversation, conversationManager.startConversation(contact));
        assertEquals(conversation, conversationManager.getConversation(contact));
    }

}