package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.core.Account;
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
        Conversation conversation = manager.startConversation(remoteContactA, false);
        ConversationEvent conversationEvent = new NewChatMessageEvent(conversation, null);

        assertEquals(remoteContactA, conversationEvent.getContact());
        assertNotSame(remoteContactB, conversationEvent.getContact());
    }

    @Test
    public void testRemoteGetContactAndConversation() throws UnknownHostException, IOException, InterruptedException {
        Account localAccount = ChatApplication.getInstance().getAccount();

        EventPool localPool = EventPool.getAppPool();
        // wait for apppool to start up
        Thread.sleep(100); 

        // create a remote eventpool
        EventPool remotePool = new EventPool();
        // and connect it to the local one
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool);
        Thread.sleep(100); // wait for thread to get up and running

        // local usernormally doesn't excist as a Contact, this is needed for testing, needs to had the username of the localuser so account.getUsername
        Contact localContact = new Contact(localAccount.getUsername(), InetAddress.getLocalHost(), false);
        // needs to be in contactlist. because contactList will act as both local and remote contactList
        localAccount.getContactList().addContact(localContact);
        // contact you will be chatting with
        Contact remoteContact = new Contact("Javache", InetAddress.getLocalHost(), true);
        // this should normally be in contactList
        localAccount.getContactList().addContact(remoteContact);
        // this wil be the local conversation manager, but it will act as both local and remote so there should also be a conversation with the local user
        ConversationManager localConversationManager = ChatApplication.getInstance().getConversationManager();
        // this should normally be in the conversationmanager
        Conversation localConversation = localConversationManager.startConversation(remoteContact, false);
        // this is the conversation the remote user has with you, it is in the local conversation manager for testing
        Conversation remoteConversation = localConversationManager.startConversation(localContact, false);
        // local user types message
        ChatMessage chatMessage = new ChatMessage(localContact.getUsername(), "Dag Javache, jij jij remoteUser!");
        ConversationEvent localEvent = new NewChatMessageEvent(localConversation, chatMessage);

        // listener on remotePpool
        DummyListener remoteListener = new DummyListener();
        remotePool.addListener(remoteListener, new TypeEventFilter(ConversationEvent.class));

        // event is raised on localpool and sended to remotePool
        localPool.raiseEvent(localEvent);
        // wait while the event gets transmitted
        Thread.sleep(200); 

        ConversationEvent remoteEvent = (ConversationEvent)remoteListener.receivedEvent;
        // not really needed because localContact's ip is already set to localhost. but hey, it is for educational purpose :p
        localAccount.getContactList().getContact(InetAddress.getLocalHost(), localContact.getUsername()).setIp(remoteEvent.getSource());

        assertTrue(remoteEvent != null);
        assertTrue(remoteEvent.isExternal());

        assertNotNull(remoteEvent.getSource());

        assertNotNull(remoteEvent.getContact());
        assertNotNull(remoteEvent.getConversation());

        assertNotSame(remoteContact, remoteEvent.getContact());
        assertEquals(localContact, remoteEvent.getContact());

        assertNotSame(localConversation, remoteEvent.getConversation());
        assertEquals(remoteConversation, remoteEvent.getConversation());
        
    }

    private class DummyListener implements EventListener {
        public Event receivedEvent;
        @Override
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
        Conversation conversationA = manager.startConversation(remoteContactA, false);
        Conversation conversationB = manager.startConversation(remoteContactB, false);

        ConversationEvent conversationEvent = new NewChatMessageEvent(conversationA, null);

        assertEquals(conversationA, conversationEvent.getConversation());
        assertNotSame(conversationB, conversationEvent.getConversation());
    }
}
