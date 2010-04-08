package ch9k.chat;

import ch9k.chat.events.NewChatMessageEvent;
import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationTest {
    private Contact contact;
    private Conversation conversation;

    public ConversationTest() {
    }

    @Before
    public void setUp() throws UnknownHostException {
        contact = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        conversation = new Conversation(contact, true);
    }

    /**
     * Test of initatedByMe method, of class Conversation.
     */
    @Test
    public void testInitatedByMe() {
        assertTrue(conversation.initatedByMe());
    }

    /**
     * Test of getContact method, of class Conversation.
     */
    @Test
    public void testGetContact() {
        assertEquals(contact, conversation.getContact());
    }

    /**
     * Test of getSubject/setSubject method, of class Conversation.
     */
    @Test
    public void testSubject() {
        ConversationSubject conversationSubject = new ConversationSubject(
                conversation, new String[] { "subject-a", "subject-b" });
        conversation.setSubject(conversationSubject);
        assertEquals(conversationSubject, conversation.getSubject());
    }

    /**
     * Test of getStartTime method, of class Conversation.
     */
    @Test
    public void testGetStartTime() throws InterruptedException {
        Thread.sleep(10); // wait a little
        assertTrue(conversation.getStartTime().before(new Date()));
    }

    /**
     * Test of addMessage method, of class Conversation.
     */
    @Test
    public void testAddMessage() {
        assertEquals(0, conversation.getMessages(10).length);
        conversation.addMessage(new ChatMessage("JPanneel", "Hey!"));
        assertEquals(1, conversation.getMessages(10).length);
    }

    /**
     * Test of getMessages method, of class Conversation.
     */
    @Test
    public void testGetMessages() {
        ChatMessage[] messages = new ChatMessage[] {
            new ChatMessage("JPanneel", "Hey!"),
            new ChatMessage("Wendy", "O dag lieverd"),
            new ChatMessage("JPanneel", "Hoe gaat het met de overkant?"),
            new ChatMessage("Wendy", "Goed, maar ik mis je wel!"),
            new ChatMessage("JPanneel", "Ik weet het :)")
        };

        assertEquals(0, conversation.getMessages(5).length);

        for(ChatMessage message : messages) {
            conversation.addMessage(message);
        }

        assertEquals(5, conversation.getMessages(10).length);
        assertEquals("Hey!", conversation.getMessages(5)[0]);
        assertEquals("Ik weet het :)", conversation.getMessages(5)[4]);
        assertEquals(3, conversation.getMessages(3).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getMessages(3)[0]);

        conversation.addMessage(new ChatMessage("Wendy", "Doei!"));
        assertEquals(5, conversation.getMessages(5).length);
        assertEquals(6, conversation.getMessages(10).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getMessages(5)[1]);
        assertEquals("Doei!", conversation.getMessages(5)[4]);
    }

    /**
     * Test of close method, of class Conversation.
     */
    @Test
    public void testClose() {
        // TODO review the generated test code and remove the default call to fail.
        fail("close() not yet implemented");
    }

    /**
     * Test of equals method, of class Conversation.
     */
    @Test
    public void testEquals() {
        assertTrue(conversation.equals(conversation)); // identity

        Conversation differentConversation = new Conversation(
                new Contact("Javache", null, true), true);
        assertFalse(conversation.equals(differentConversation));
    }

    /**
     * Test of hashCode method, of class Conversation.
     */
    @Test
    public void testHashCode() {
        assertEquals(conversation.hashCode(), conversation.hashCode());
        
        Conversation differentConversation = new Conversation(
                new Contact("Javache", null, true), true);
        assertNotSame(conversation.hashCode(), differentConversation.hashCode());
    }

    /**
     * Test of handleEvent method, of class Conversation.
     */
    @Test
    public void testHandleEvent() {
        ChatMessage chatMessage = new ChatMessage("Javache", "lama! lama!");
        NewChatMessageEvent messageEvent = new NewChatMessageEvent(chatMessage, conversation);
        EventPool.getAppPool().raiseEvent(messageEvent);
        
        assertEquals(1, conversation.getMessages(10).length);
        assertEquals(conversation.getMessages(1)[0], "lama! lama!");
    }
}