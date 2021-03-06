package ch9k.chat;

import java.util.Date;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChatMessageTest {
    private String author;
    private String text;
    private ChatMessage chatMessage;

    @Before
    public void setUp() {
        author = "JPanneel";
        text = "Hey! You!";
        chatMessage = new ChatMessage(author, text);
    }

    /**
     * Test of getText method, of class ChatMessage.
     */
    @Test
    public void testGetText() {
        assertEquals(text, chatMessage.getText());
    }

    /**
     * Test the getRawText method.
     */
    @Test
    public void testGetRawText() {
        ChatMessage message1 = new ChatMessage("jasper", "<p>foobar</p>");
        assertEquals("foobar", message1.getRawText());

        ChatMessage message2 = new ChatMessage("jasper", "<script>");
        assertEquals("", message2.getRawText());

        ChatMessage message3 = new ChatMessage("jasper", "no HTML here");
        assertEquals("no HTML here", message3.getRawText());
    }

    /**
     * Test of getAuthor method, of class ChatMessage.
     */
    @Test
    public void testGetAuthor() {
        assertEquals(author, chatMessage.getAuthor());
    }

    /**
     * Test of getTime method, of class ChatMessage.
     */
    @Test
    public void testGetTime() throws InterruptedException {
        Thread.sleep(5);
        assertTrue(chatMessage.getTime().before(new Date()));
    }

    /**
     * Test of compareTo method, of class ChatMessage.
     * @throws InterruptedException
     */
    @Test
    public void testCompareTo() throws InterruptedException {
        Thread.sleep(10); // so we get an older message
        ChatMessage newerChatMessage = new ChatMessage("Javache", "JaJa!");

        assertEquals(0,chatMessage.compareTo(chatMessage));
        assertTrue(chatMessage.compareTo(newerChatMessage) < 0);
        assertTrue(newerChatMessage.compareTo(chatMessage) > 0);
    }

    /**
     * Test of equals method, of class ChatMessage.
     */
    @Test
    public void testEquals() {
        assertTrue(chatMessage.equals(chatMessage));
        assertFalse(chatMessage.equals(new ChatMessage(author, "JaJa!")));
        assertFalse(chatMessage.equals(new ChatMessage("Javache", text)));
        assertFalse(chatMessage.equals(new ChatMessage("Javache", "JaJa!")));
    }

    /**
     * Test of hashCode method, of class ChatMessage.
     */
    @Test
    public void testHashCode() throws InterruptedException {
        ChatMessage differentMessage = new ChatMessage("Javache", "JaJa!");
        assertNotSame(chatMessage.hashCode(), differentMessage.hashCode());
        
        Thread.sleep(5);
        differentMessage = new ChatMessage("JPanneel", "Hey! You!");
        assertNotSame(chatMessage.hashCode(), differentMessage.hashCode());
    }

     /**
     * Test of toString method, of class ChatMessage.
     */
    @Test
    public void testToString() {
        String result = chatMessage.toString();
        // use a regex for matching the date
        assertTrue(result.matches("^<[0-9:]+> " + Pattern.quote(author) +
                ": " + Pattern.quote(text)));
    }
}
