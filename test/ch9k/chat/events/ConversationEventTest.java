/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ConversationEventTest {

    public ConversationEventTest() {
    }

    /**
     * Test of getConversation method, of class ConversationEvent.
     */
    @Test
    public void testGetConversation() {
        System.out.println("getConversation");
        Conversation conversation = new Conversation(new Contact("Javache", null, true), true);
        ConversationEvent instance = new ConversationEventImpl(conversation);
        assertEquals(conversation, instance.getConversation());
    }

    public class ConversationEventImpl extends ConversationEvent {
        public ConversationEventImpl(Conversation conversation) {
            super(conversation);
        }
    }

}