package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TestListener;
import ch9k.network.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConversationEventTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }

    @After
    public void tearDown() {
        EventPool.getAppPool().clearListeners();
        ChatApplication.getInstance().getAccount().getContactList().clear();
        ChatApplication.getInstance().getConversationManager().clear();
    }

    /**
     * Test of getContact method, of class ConversationEvent.
     * @throws UnknownHostException 
     */
    @Test
    public void testGetContact() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"));
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"));
        
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
        // get the local app-pool and let it start
        EventPool localPool = EventPool.getAppPool();
        Thread.sleep(100);

        // create a remote eventpool and connect it to the local one
        EventPool remotePool = new EventPool();
        // and connect it to the local one
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool, null);
        Thread.sleep(100); // wait for thread to get up and running

        // create a contact out of the local user (as seen from the other side)
        Account localAccount = ChatApplication.getInstance().getAccount();
        Contact localContact = new Contact(localAccount.getUsername(), InetAddress.getLocalHost());
        Contact remoteContact = new Contact("Javache", InetAddress.getLocalHost());

        // add both of these contacts to our list, so we can look them up later
        ContactList contactList = localAccount.getContactList();
        contactList.addContact(localContact);
        contactList.addContact(remoteContact);

        // conversation manager will contain the conversation with the remote user, as
        // well as the conversation with the local one
        ConversationManager conversationManager = ChatApplication.getInstance().getConversationManager();
        Conversation localConversation = conversationManager.startConversation(remoteContact, false);
        Conversation remoteConversation = conversationManager.startConversation(localContact, false);

        // register the remoteConversation specifically with the remote pool
        // it already registered with the local pool during construction
        // let's hope that doesn't bring too much problems...
        remotePool.addListener(remoteConversation, new ConversationEventFilter(remoteConversation));

        // remote conversation should not listen to localPool
        localPool.removeListener(remoteConversation);

        // now everything is setup, and a user can type a message
        ChatMessage chatMessage = new ChatMessage(localContact.getUsername(), "Dag Javache, jij jij remoteUser!");
        ConversationEvent localEvent = new NewChatMessageEvent(localConversation, chatMessage);

        // create a listener on the remote pool
        TestListener remoteListener = new TestListener();
        remotePool.addListener(remoteListener, new EventFilter(ConversationEvent.class));

        // raise the event on the local pool, should get sent to remotePool too
        localPool.raiseEvent(localEvent);
        Thread.sleep(100);

        // let's check the results!
        ConversationEvent remoteEvent = (ConversationEvent)remoteListener.lastReceivedEvent;
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

    /**
     * Test of getConversation method, of class ConversationEvent.
     */
    @Test
    public void testGetConversation() throws UnknownHostException {
        Contact remoteContactA = new Contact("Javache", InetAddress.getByName("ugent.be"));
        Contact remoteContactB = new Contact("JPanneel", InetAddress.getByName("google.be"));

        ConversationManager manager = ChatApplication.getInstance().getConversationManager();
        Conversation conversationA = manager.startConversation(remoteContactA, false);
        Conversation conversationB = manager.startConversation(remoteContactB, false);

        ConversationEvent conversationEvent = new NewChatMessageEvent(conversationA, null);

        assertEquals(conversationA, conversationEvent.getConversation());
        assertNotSame(conversationB, conversationEvent.getConversation());
    }
}
