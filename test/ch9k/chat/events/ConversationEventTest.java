package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.core.ChatApplication;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationEventTest {
    /**
     * Test of getContact method, of class ConversationEvent.
     * @throws UnknownHostException 
     */
    @Test
    public void testGetContact() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        
        // add these users to the contactlist
        ContactList list = ChatApplication.getInstance().getAccount().getContactList();
        list.addContact(remoteContactA);
        list.addContact(remoteContactB);

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Conversation conversation = manager.startConversation(remoteContactA);
        ConversationEvent conversationEvent = new NewChatMessageEvent(conversation, null);

        assertEquals(remoteContactA, conversationEvent.getContact());
        assertNotSame(remoteContactB, conversationEvent.getContact());
    }

    /**
     * Test of getConversation method, of class ConversationEvent.
     */
    @Test
    public void testGetConversation() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"), true);

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Conversation conversationA = manager.startConversation(remoteContactA);
        Conversation conversationB = manager.startConversation(remoteContactB);

        ConversationEvent conversationEvent = new NewChatMessageEvent(conversationA, null);

        assertEquals(conversationA, conversationEvent.getConversation());
        assertNotSame(conversationB, conversationEvent.getConversation());
    }
}
