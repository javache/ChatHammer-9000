/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class NewConversationSubjectEventTest {

    public NewConversationSubjectEventTest() {
    }

    /**
     * Test of getConversationSubject method, of class NewConversationSubjectEvent.
     */
    @Test
    public void testGetConversationSubject() throws UnknownHostException {
        Contact contact = new Contact("Javache", InetAddress.getLocalHost(), false);
        Conversation conversation = new Conversation(contact, true);
        String[] subjects = new String[] {"seks", "stal", "dieren" };
        ConversationSubject conversationSubject =
                new ConversationSubject(conversation, subjects);
        NewConversationSubjectEvent newConversationSubjectEvent =
                new NewConversationSubjectEvent(conversation, conversationSubject);
        assertEquals(conversationSubject,
                newConversationSubjectEvent.getConversationSubject());
    }

}
