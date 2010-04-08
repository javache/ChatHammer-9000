package ch9k.chat;

import ch9k.chat.events.NewChatMessageEvent;
import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.junit.After;
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

    @After
    public void tearDown() {
        contact = null;
    }

    /**
     * Test of initatedByMe method, of class Conversation.
     */
    @Test
    public void testInitatedByMe() {
        System.out.println("initatedByMe");
        assertTrue(conversation.initatedByMe());
    }

    /**
     * Test of getContact method, of class Conversation.
     */
    @Test
    public void testGetContact() {
        System.out.println("getContact");
        assertEquals(contact, conversation.getContact());
    }

    /**
     * Test of getSubject method, of class Conversation.
     */
    @Test
    public void testGetSubject() {
        System.out.println("getSubject");
        ConversationSubject conversationSubject = new ConversationSubject(conversation, new String[2]);
        conversation.setSubject(conversationSubject);
        assertEquals(conversationSubject, conversation.getSubject());
    }

    /**
     * Test of setSubject method, of class Conversation.
     */
    @Test
    public void testSetSubject() {
        System.out.println("setSubject");
        ConversationSubject conversationSubject = new ConversationSubject(conversation, new String[2]);
        conversation.setSubject(conversationSubject);
        assertEquals(conversationSubject, conversation.getSubject());
    }

    /**
     * Test of getStartTime method, of class Conversation.
     */
    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");
        // this Java thing is so fast it gets the same time!...
        assertTrue(conversation.getStartTime().before(new Date()) || conversation.getStartTime().equals(new Date()));
    }

    /**
     * Test of addChatMessage method, of class Conversation.
     */
    @Test
    public void testAddChatMessage() {
        System.out.println("addChatMessage");
        assertEquals(0, conversation.getChatMessages(10).length);
        conversation.addChatMessage(new ChatMessage("JPanneel", "Hey!"));
        assertEquals(1, conversation.getChatMessages(10).length);
    }

    /**
     * Test of getChatMessages method, of class Conversation.
     */
    @Test
    public void testGetChatMessages() {
        System.out.println("getChatMessages");
        assertEquals(0, conversation.getChatMessages(10).length);
        conversation.addChatMessage(new ChatMessage("JPanneel", "Hey!"));
        conversation.addChatMessage(new ChatMessage("Wendy", "O dag lieverd"));
        conversation.addChatMessage(new ChatMessage("JPanneel", "Hoe gaat het met de overkant?"));
        conversation.addChatMessage(new ChatMessage("Wendy", "Goed, maar ik mis je wel!"));
        conversation.addChatMessage(new ChatMessage("JPanneel", "Ik weet het :)"));
        assertEquals(5, conversation.getChatMessages(10).length);
        assertEquals("Hey!", conversation.getChatMessages(10)[0]);
        assertEquals("Ik weet het :)", conversation.getChatMessages(10)[4]);
        assertEquals(3, conversation.getChatMessages(3).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getChatMessages(3)[0]);
    }

    /**
     * Test of close method, of class Conversation.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        Conversation instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("close() not yet implemented");
    }

    /**
     * Test of equals method, of class Conversation.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        assertTrue(conversation.equals(conversation));
        assertFalse(conversation.equals(new Conversation(new Contact("Javache", null, true), true)));
    }

    /**
     * Test of hashCode method, of class Conversation.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        assertEquals(conversation.hashCode(), conversation.hashCode());
        Conversation conversation1 = new Conversation(new Contact("Javache", null, true), true);
        assertNotSame(conversation.hashCode(), conversation1.hashCode());
    }

        /**
     * Test of handleEvent method, of class Conversation.
     */
    @Test
    public void testHandleEvent() {
        System.out.println("handleEvent");
        ChatMessage chatMessage = new ChatMessage("Javache", "lama! lama!");
        NewChatMessageEvent newChatMessageEvent = new NewChatMessageEvent(chatMessage, conversation);
        EventPool.getAppPool().raiseEvent(newChatMessageEvent);
        //assertTrue(conversation.getChatMessages(1).length == 1);
        assertEquals(conversation.getChatMessages(1)[0], "lama! lama!");
    }


}