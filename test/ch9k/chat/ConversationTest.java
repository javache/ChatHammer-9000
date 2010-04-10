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
import ch9k.eventpool.TestListener;
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
        ChatApplication.getInstance().getAccount().getContactList().clear();
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
     * @throws InterruptedException
     */
    @Test
    public void testClose() throws InterruptedException {
        TestListener testListener = new TestListener();
        EventPool.getAppPool().addListener(testListener, new TypeEventFilter(CloseConversationEvent.class));
        conversation.close();
        Thread.sleep(100);

        CloseConversationEvent closeConversationEvent = (CloseConversationEvent)testListener.lastReceivedEvent;
        assertEquals(contact, closeConversationEvent.getContact());

        // wait so everything else properly times out
        Thread.sleep(400);
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
     * @throws UnknownHostException 
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
      * @throws UnknownHostException 
      * @throws InterruptedException
      * @throws IOException
     */
    @Test
    public void testHandleEvent() throws UnknownHostException, IOException, InterruptedException {
        // get the local app-pool and let it start
        EventPool localPool = EventPool.getAppPool();
        Thread.sleep(100);

        // create a remote eventpool and connect it to the local one
        EventPool remotePool = new EventPool();
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool);
        Thread.sleep(100);

        // create a contact out of the local user (as seen from the other side)
        Account localAccount = ChatApplication.getInstance().getAccount();
        Contact localContact = new Contact(localAccount.getUsername(), InetAddress.getLocalHost(), false);
        Contact remoteContact = new Contact("Javache", InetAddress.getLocalHost(), true);

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

        // now everything is setup, and a user can type a message
        ChatMessage chatMessage = new ChatMessage(localContact.getUsername(), "Dag Javache, jij jij remoteUser!");
        ConversationEvent localEvent = new NewChatMessageEvent(localConversation, chatMessage);

        // raise the event on the local pool, should get sent to remotePool too
        localPool.raiseEvent(localEvent);
        Thread.sleep(250);

        // let's check the results!
        assertEquals(1, remoteConversation.getMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", remoteConversation.getMessages(1)[0]);

        assertEquals(1, localConversation.getMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", localConversation.getMessages(1)[0]);
    }
}
