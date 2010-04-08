/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ConversationEventFilterTest {

    public ConversationEventFilterTest() {
    }

    /**
     * Test of accept method, of class ConversationEventFilter.
     */
    @Test
    public void testAccept() throws UnknownHostException {
        System.out.println("accept");
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        Contact contact2 = new Contact("Javache", InetAddress.getByName("google.be"), true);
        Conversation conversation1 = new Conversation(contact1, true);
        Conversation conversation2 = new Conversation(contact2, true);
        ConversationEventFilter conversationEventFilter = new ConversationEventFilter(conversation1);
        ChatMessage chatMessage = new ChatMessage(contact2.getUsername(), "lama! lama!");
        Event event1 = new NewChatMessageEvent(chatMessage, conversation1);
        Event event2 = new NewChatMessageEvent(chatMessage, conversation2);

        assertTrue(conversationEventFilter.accept(event1));
        assertFalse(conversationEventFilter.accept(event2));
    }

}