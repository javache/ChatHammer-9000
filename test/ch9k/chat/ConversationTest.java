package ch9k.chat;

import ch9k.chat.events.CloseConversationEvent;
import ch9k.chat.events.ConversationEvent;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.NewChatMessageEvent;
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
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
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
        EventPool.getAppPool().clearListeners();
    }

    /**
     * Test of initatedByMe method, of class Conversation.
     */
    @Test
    public void testInitatedByMe() {
        assertTrue(conversation.initatedByMe());
    }

    /**
     * Test of getContact method, of class Conversation.
     */
    @Test
    public void testGetContact() {
        assertEquals(contact, conversation.getContact());
    }

    /**
     * Test of getSubject/setSubject method, of class Conversation.
     */
    @Test
    public void testSubject() {
        ConversationSubject conversationSubject = new ConversationSubject(
                conversation, new String[] { "subject-a", "subject-b" });
        conversation.setSubject(conversationSubject);
        assertEquals(conversationSubject, conversation.getSubject());
    }

    /**
     * Test of getStartTime method, of class Conversation.
     */
    @Test
    public void testGetStartTime() throws InterruptedException {
        Thread.sleep(10); // wait a little
        assertTrue(conversation.getStartTime().before(new Date()));
    }

    /**
     * Test of addMessage method, of class Conversation.
     */
    @Test
    public void testAddMessage() {
        assertEquals(0, conversation.getMessages(10).length);
        conversation.addMessage(new ChatMessage("JPanneel", "Hey!"));
        assertEquals(1, conversation.getMessages(10).length);
    }

    /**
     * Test of getMessages method, of class Conversation.
     */
    @Test
    public void testGetMessages() {
        ChatMessage[] messages = new ChatMessage[] {
            new ChatMessage("JPanneel", "Hey!"),
            new ChatMessage("Wendy", "O dag lieverd"),
            new ChatMessage("JPanneel", "Hoe gaat het met de overkant?"),
            new ChatMessage("Wendy", "Goed, maar ik mis je wel!"),
            new ChatMessage("JPanneel", "Ik weet het :)"),
            new ChatMessage("Wendy", "Doei!")
        };

        assertEquals(0, conversation.getMessages(5).length);

        for (int i = 0; i < 5; i++) {
            conversation.addMessage(messages[i]);
        }

        assertEquals(5, conversation.getMessages(10).length);
        assertEquals("Hey!", conversation.getMessages(5)[0]);
        assertEquals("Ik weet het :)", conversation.getMessages(5)[4]);
        assertEquals(3, conversation.getMessages(3).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getMessages(3)[0]);

        conversation.addMessage(messages[5]);
        assertEquals(5, conversation.getMessages(5).length);
        assertEquals(6, conversation.getMessages(10).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getMessages(5)[1]);
        assertEquals("Doei!", conversation.getMessages(5)[4]);
    }

    /**
     * Test of close method, of class Conversation.
     */
    @Test
    public void testClose() throws InterruptedException {
        DummyListener dummyListener = new DummyListener();
        EventPool.getAppPool().addListener(dummyListener, new TypeEventFilter(CloseConversationEvent.class));
        conversation.close();
        Thread.sleep(100);

        CloseConversationEvent closeConversationEvent = (CloseConversationEvent)dummyListener.receivedEvent;
        assertEquals(conversation, closeConversationEvent.getConversation());
    }

    private class DummyListener implements EventListener {
        public Event receivedEvent;
        @Override
        public void handleEvent(Event event) {
            receivedEvent = event;
        }
    }

    /**
     * Test of equals method, of class Conversation.
     */
    @Test
    public void testEquals() {
        assertTrue(conversation.equals(conversation)); // identity

        Conversation differentConversation = new Conversation(
                new Contact("Javache", null, true), true);
        assertFalse(conversation.equals(differentConversation));
    }

    /**
     * Test of hashCode method, of class Conversation.
     */
    @Test
    public void testHashCode() throws UnknownHostException {
        assertEquals(conversation.hashCode(), conversation.hashCode());
        
        Conversation differentConversation = new Conversation(
                new Contact("Javache", InetAddress.getByName("ugent.be"), false), true);
        assertNotSame(conversation.hashCode(), differentConversation.hashCode());
    }


     /**
     * Test of handleEvent method, of class Conversation.
     * Tests both local and remote side
     * @throws InterruptedException
     */
    @Test
    public void testHandleEvent() throws UnknownHostException, IOException, InterruptedException {
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

        // register remoteConversation as listener to remotePool
        remotePool.addListener(remoteConversation, new ConversationEventFilter(remoteConversation));
        // register localConversation as listener to localPool
        remotePool.addListener(remoteConversation, new ConversationEventFilter(remoteConversation));

        // event is raised on localpool and sended to remotePool
        localPool.raiseEvent(localEvent);
        // wait while the event gets transmitted
        Thread.sleep(500);

        assertEquals(1, remoteConversation.getMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", remoteConversation.getMessages(1)[0]);

        assertEquals(1, localConversation.getMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", localConversation.getMessages(1)[0]);
    }

}
