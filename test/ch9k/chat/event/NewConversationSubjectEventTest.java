/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat.event;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.core.ChatApplication;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewConversationSubjectEventTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }

    /**
     * Test of getConversationSubject method, of class NewConversationSubjectEvent.
     */
    @Test
    public void testGetConversationSubject() throws UnknownHostException {
        Contact contact = new Contact("Javache", InetAddress.getLocalHost());
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
