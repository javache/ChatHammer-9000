package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConversationEventFilterTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }

    /**
     * Test of accept method, of class ConversationEventFilter.
     */
    @Test
    public void testAccept() throws UnknownHostException {
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"));
        Contact contact2 = new Contact("Javache", InetAddress.getByName("google.be"));
        Conversation conversation1 = new Conversation(contact1, true);
        Conversation conversation2 = new Conversation(contact2, true);
        
        ConversationEventFilter conversationEventFilter = new ConversationEventFilter(conversation1);
        ChatMessage chatMessage = new ChatMessage(contact1.getUsername(), "lama! lama!");
        Event event1 = new NewChatMessageEvent(conversation1, chatMessage);
        Event event2 = new NewChatMessageEvent(conversation2, chatMessage);

        assertTrue(conversationEventFilter.accept(event1));
        assertFalse(conversationEventFilter.accept(event2));
    }

}
