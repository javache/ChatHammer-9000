package ch9k.chat.events;

import ch9k.chat.Contact;
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

    public ConversationEventTest() {
    }

    /**
     * Test of getContact method, of class ConversationEvent.
     */
    @Test
    public void testGetContact() throws UnknownHostException {
        // contact1 acts like local user
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        // will chat with contact2
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        // just another contact
        Contact contact3 = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        // the non lacal users are in the contactList
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact2);
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact3);

        ConversationEvent conversationEvent = new NewChatMessageEvent(null, contact2);

        assertNotSame(contact1, conversationEvent.getContact());
        assertEquals(contact2, conversationEvent.getContact());
        assertNotSame(contact3, conversationEvent.getContact());
    }

        /**
     * Test of getConversation method, of class ConversationEvent.
     */
    @Test
    public void testGetConversation() throws UnknownHostException {
        // contact1 acts like local user
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        // will chat with contact2
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        // just another contact
        Contact contact3 = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        // the non lacal users are in the contactList
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact2);
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact3);

        ChatApplication.getInstance().getConversationManager().startConversation(contact2);

        ConversationEvent conversationEvent = new NewChatMessageEvent(null, contact2);

        assertEquals(ChatApplication.getInstance().getConversationManager().getConversation(contact2),
                conversationEvent.getConversation());
        assertNotSame(ChatApplication.getInstance().getConversationManager().getConversation(contact3),
                conversationEvent.getContact());
    }
}