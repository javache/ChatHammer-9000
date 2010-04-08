package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class NewChatMessageEventTest {

    /**
     * Test of getChatMessage method, of class NewChatMessageEvent.
     */
    @Test
    public void testGetChatMessage() throws UnknownHostException {
        Conversation conversation = new Conversation(
                new Contact("Javache", InetAddress.getByName("thinkjavache.be"), false), true);
        ChatMessage chatMessage1 = new ChatMessage("JPanneel", "tada!");
        ChatMessage chatMessage2 = new ChatMessage("JPanneel", "Oi Oi Oi");
        NewChatMessageEvent newChatMessageEvent1 = new NewChatMessageEvent(chatMessage1, conversation);
        NewChatMessageEvent newChatMessageEvent2 = new NewChatMessageEvent(chatMessage2, conversation);
        
        assertEquals(chatMessage1, newChatMessageEvent1.getChatMessage());
        assertNotSame(chatMessage1, newChatMessageEvent2.getChatMessage());
    }

}