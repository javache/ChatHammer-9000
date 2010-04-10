package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import ch9k.network.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationEventTest {
    /**
     * Test of getContact method, of class ConversationEvent.
     * @throws UnknownHostException 
     */
    @Test
    public void testGetContact() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"), true);
        
        // add these users to the contactlist
        ContactList list = ChatApplication.getInstance().getAccount().getContactList();
        list.addContact(remoteContactA);
        list.addContact(remoteContactB);

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Conversation conversation = manager.startConversation(remoteContactA);
        ConversationEvent conversationEvent = new NewChatMessageEvent(conversation, null);

        assertEquals(remoteContactA, conversationEvent.getContact());
        assertNotSame(remoteContactB, conversationEvent.getContact());
    }

    @Test
    public void testRemoteGetContact() throws UnknownHostException, IOException, InterruptedException {
        EventPool localPool = EventPool.getAppPool();
        Thread.sleep(100); // wait for apppool to start up

        // create a remote eventpool
        EventPool remotePool = new EventPool();
        // and connect it to the local one
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool);
        Thread.sleep(100);

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Contact contact = new Contact("Javache", InetAddress.getLocalHost(), true);
        Conversation localConversation = manager.startConversation(contact);

        DummyListener remoteListener = new DummyListener();
        remotePool.addListener(remoteListener, new TypeEventFilter(ConversationEvent.class));

        ConversationEvent localEvent = new NewChatMessageEvent(localConversation, null);
        localPool.raiseEvent(localEvent);
        Thread.sleep(1000); // wait while the event gets transmitted

        ConversationEvent remoteEvent = (ConversationEvent)remoteListener.receivedEvent;
        assertTrue(remoteEvent != null);
        assertTrue(remoteEvent.isExternal());

        assertNotSame(contact, remoteEvent.getContact());
        assertNotSame(localConversation, remoteEvent.getConversation());
    }

    private class DummyListener implements EventListener {
        public Event receivedEvent;
        public void handleEvent(Event event) {
            receivedEvent = event;
        }
    }

    /**
     * Test of getConversation method, of class ConversationEvent.
     */
    @Test
    public void testGetConversation() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"), true);
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"), true);

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Conversation conversationA = manager.startConversation(remoteContactA);
        Conversation conversationB = manager.startConversation(remoteContactB);

        ConversationEvent conversationEvent = new NewChatMessageEvent(conversationA, null);

        assertEquals(conversationA, conversationEvent.getConversation());
        assertNotSame(conversationB, conversationEvent.getConversation());
    }
}
