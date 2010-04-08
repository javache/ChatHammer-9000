/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class NewChatMessageEventTest {

    public NewChatMessageEventTest() {
    }

    /**
     * Test of getChatMessage method, of class NewChatMessageEvent.
     */
    @Test
    public void testGetChatMessage() {
        System.out.println("getChatMessage");
        ChatMessage chatMessage1 = new ChatMessage("JPanneel", "tada!");
        ChatMessage chatMessage2 = new ChatMessage("JPanneel", "Oi Oi Oi");
        NewChatMessageEvent newChatMessageEvent1 = new NewChatMessageEvent(chatMessage1,null);
        NewChatMessageEvent newChatMessageEvent2 = new NewChatMessageEvent(chatMessage2,null);
        assertEquals(chatMessage1, newChatMessageEvent1.getChatMessage());
        assertNotSame(chatMessage1, newChatMessageEvent2.getChatMessage());
    }

}