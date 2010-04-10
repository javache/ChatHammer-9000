package ch9k.chat;

import ch9k.chat.events.ConversationEvent;
import ch9k.chat.events.NewConversationEvent;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.EventPool;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationManagerTest {

    private ConversationManager conversationManager;

    @Before
    public void setUp() {
        conversationManager = new ConversationManager();
    }

    /**
     * Test of startConversation method, of class ConversationManager.
     */
    @Test
    public void testStartConversation() {
        Contact contact = new Contact("Javache", null, true);
        Conversation conversation = new Conversation(contact, true);
        assertEquals(conversation, conversationManager.startConversation(contact, false));
        assertEquals(conversation, conversationManager.getConversation(contact));
    }

    /**
     * Test of closeConversation method, of class ConversationManager.
     */
    @Test
    public void testCloseConversation() {
        Contact contact = new Contact("Javache", null, true);
        Conversation conversation = conversationManager.startConversation(contact, true);
        assertEquals(conversation, conversationManager.getConversation(contact));
        conversationManager.closeConversation(contact);
        assertNull(conversationManager.getConversation(contact));
    }

    /**
     * Test of getConversation method, of class ConversationManager.
     */
    @Test
    public void testGetConversation() {
        Contact contact = new Contact("Javache", null, true);
        Conversation conversation = new Conversation(contact, true);
        assertEquals(conversation, conversationManager.startConversation(contact, false));
        assertEquals(conversation, conversationManager.getConversation(contact));
    }

     /**
     * Test of handleEvent method, of class Conversation.
     * Tests both local and remote side
     */
    @Test
    public void testLocalHandleEvent() throws UnknownHostException, IOException, InterruptedException {
        Account localAccount = ChatApplication.getInstance().getAccount();
        ConversationManager localConversationManager = ChatApplication.getInstance().getConversationManager();
        EventPool localPool = EventPool.getAppPool();
        // wait for apppool to start up
        Thread.sleep(100);

        Contact remoteContact = new Contact("Javache", InetAddress.getByName("google.be"), true);
        localAccount.getContactList().addContact(remoteContact);
        
        ConversationEvent localCreateEvent = new NewConversationEvent(remoteContact);

        localPool.raiseEvent(localCreateEvent);
        // wait while the event gets transmitted
        Thread.sleep(500);

        assertNotNull(localConversationManager.getConversation(remoteContact));

        // this will raise an CloseConversationEvent
        localConversationManager.getConversation(remoteContact).close();
        Thread.sleep(500);
        
        assertNull(localConversationManager.getConversation(remoteContact));
        
    }
}