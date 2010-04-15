package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.core.ChatApplication;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jens Panneel
 */
public class NewChatMessageEventTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }
    
    /**
     * Test of getChatMessage method, of class NewChatMessageEvent.
     */
    @Test
    public void testGetChatMessage() throws UnknownHostException {
        Conversation conversation = new Conversation(
                new Contact("Javache", InetAddress.getByName("thinkjavache.be"), false), true);
        ChatMessage chatMessage1 = new ChatMessage("JPanneel", "tada!");
        ChatMessage chatMessage2 = new ChatMessage("JPanneel", "Oi Oi Oi");
        NewChatMessageEvent newChatMessageEvent1 = new NewChatMessageEvent(conversation, chatMessage1);
        NewChatMessageEvent newChatMessageEvent2 = new NewChatMessageEvent(conversation, chatMessage2);
        
        assertEquals(chatMessage1, newChatMessageEvent1.getChatMessage());
        assertNotSame(chatMessage1, newChatMessageEvent2.getChatMessage());
    }
}
