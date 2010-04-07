package ch9k.chat;

import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ChatMessageTest {
    private String writer;
    private String text;
    private ChatMessage chatMessage;

    public ChatMessageTest() {
    }

    @Before
    public void setUp() {
        writer = "JPanneel";
        text = "Hey! You!";
        chatMessage = new ChatMessage(writer, text);
    }

    @After
    public void tearDown() {
        writer = null;
        text = null;
        chatMessage = null;
    }

    /**
     * Test of getText method, of class ChatMessage.
     */
    @Test
    public void testGetText() {
        System.out.println("getText");
        assertEquals(chatMessage.getText(), text);
    }

    /**
     * Test of getWriter method, of class ChatMessage.
     */
    @Test
    public void testGetWriter() {
        System.out.println("getWriter");
        assertEquals(chatMessage.getWriter(), writer);
    }

    /**
     * Test of getTime method, of class ChatMessage.
     */
    @Test
    public void testGetTime() {
        System.out.println("getTime");
        assertTrue(chatMessage.getTime().before(new Date()) || chatMessage.getTime().equals(new Date()));
    }

    /**
     * Test of compareTo method, of class ChatMessage.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        assertEquals(chatMessage.compareTo(chatMessage), 0);
        for(int i = 0; i < 1000000; i++){
            // so there is a difference in creation time
        }
        ChatMessage chatMessage2 = new ChatMessage("Javache", "JaJa!");
        assertTrue(chatMessage.compareTo(chatMessage2) < 0);
        assertTrue(chatMessage2.compareTo(chatMessage) > 0);
    }

    /**
     * Test of equals method, of class ChatMessage.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        assertTrue(chatMessage.equals(chatMessage));
        assertFalse(chatMessage.equals(new ChatMessage(writer, "JaJa!")));
        assertFalse(chatMessage.equals(new ChatMessage("Javache", text)));
        assertFalse(chatMessage.equals(new ChatMessage("Javache", "JaJa!")));
    }

    /**
     * Test of hashCode method, of class ChatMessage.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        assertEquals(chatMessage.hashCode(), chatMessage.hashCode());
        ChatMessage chatMessage2 = new ChatMessage("Javache", "JaJa!");
        assertNotSame(chatMessage.hashCode(), chatMessage2.hashCode());
    }

}