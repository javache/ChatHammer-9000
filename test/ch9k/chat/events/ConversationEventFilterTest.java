package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationEventFilterTest {

    public ConversationEventFilterTest() {
    }

    /**
     * Test of accept method, of class ConversationEventFilter.
     */
    @Test
    public void testAccept() throws UnknownHostException {
        // contact1 acts like local user
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        // will chat with contact2
        Contact contact2 = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        // just another contact
        Contact contact3 = new Contact("Jaspervdj", InetAddress.getByName("4chan.org"), true);
        // the non lacal users are in the contactList
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact2);
        ChatApplication.getInstance().getAccount().getContactList().addContact(contact3);

        ConversationEventFilter conversationEventFilter = new ConversationEventFilter(contact2);

        Event event1 = new NewChatMessageEvent(null, contact2);
        Event event2 = new NewChatMessageEvent(null, contact3);

        assertTrue(conversationEventFilter.accept(event1));
        assertFalse(conversationEventFilter.accept(event2));
    }

}