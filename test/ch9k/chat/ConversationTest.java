package ch9k.chat;

import ch9k.chat.event.ConversationEvent;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.NewChatMessageEvent;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.EventPool;
import ch9k.network.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConversationTest {
    private Contact contact;
    private Conversation conversation;

    @Before
    public void setUp() throws UnknownHostException {
        ChatApplication.getInstance().performTestLogin();
        contact = new Contact("JPanneel", InetAddress.getByName("google.be"));
        conversation = new Conversation(contact, true);
    }

    @After
    public void tearDown() {
        EventPool.getAppPool().clearListeners();
        ChatApplication.getInstance().logoff(false);
    }

    /**
     * Test of initatedByMe method, of class Conversation.
     */
    @Test
    public void testInitatedByMe() {
        assertTrue(conversation.isInitiatedByMe());
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
                new String[] { "subject-a", "subject-b" });
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
    public void testAddMessage() throws InterruptedException {
        assertEquals(0, conversation.getChatMessages(10).length);
        ChatMessage chatMessage = new ChatMessage("JPanneel", "Hey!");
        NewChatMessageEvent chatMessageEvent = new NewChatMessageEvent(conversation, chatMessage);
        EventPool.getAppPool().raiseNetworkEvent(chatMessageEvent);
        Thread.sleep(100);
        assertEquals(1, conversation.getChatMessages(10).length);
    }

    /**
     * Test of getChatMessages method, of class Conversation.
     */
    @Test
    public void testGetMessages() throws InterruptedException {
        ChatMessage[] messages = new ChatMessage[] {
            new ChatMessage("JPanneel", "Hey!"),
            new ChatMessage("Wendy", "O dag lieverd"),
            new ChatMessage("JPanneel", "Hoe gaat het met de overkant?"),
            new ChatMessage("Wendy", "Goed, maar ik mis je wel!"),
            new ChatMessage("info", "haha, iz in your code", true),
            new ChatMessage("JPanneel", "Ik weet het :)"),
            new ChatMessage("Wendy", "Doei!")
        };

        assertEquals(0, conversation.getChatMessages(5).length);

        EventPool eventPool = EventPool.getAppPool();

        for (int i = 0; i < 6; i++) {
            EventPool.getAppPool().raiseNetworkEvent(
                    new NewChatMessageEvent(conversation, messages[i]));
            Thread.sleep(100);
        }

        assertEquals(5, conversation.getChatMessages(10).length);
        assertEquals("Hey!", conversation.getChatMessages(5)[0].getText());
        assertEquals("Ik weet het :)", conversation.getChatMessages(5)[4].getText());
        assertEquals(3, conversation.getChatMessages(3).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getChatMessages(3)[0].getText());

        EventPool.getAppPool().raiseNetworkEvent(
                    new NewChatMessageEvent(conversation, messages[6]));
        Thread.sleep(100);
        assertEquals(5, conversation.getChatMessages(5).length);
        assertEquals(6, conversation.getChatMessages(10).length);
        assertEquals("Hoe gaat het met de overkant?", conversation.getChatMessages(5)[1].getText());
        assertEquals("Doei!", conversation.getChatMessages(5)[4].getText());
    }

    /**
     * Test of close method, of class Conversation.
     * @throws InterruptedException
     */
    @Test
    public void testClose() throws InterruptedException {
        // wait for the window to be created. (is in another thread)
        // otherwise nullpointers
        Thread.sleep(500);
        JFrame window = conversation.getWindow();
        conversation.close(true);

        Thread.sleep(100);
        
    }

    /**
     * Test of equals method, of class Conversation.
     */
    @Test
    public void testEquals() throws UnknownHostException {
        assertTrue(conversation.equals(conversation)); // identity

        Conversation differentConversation = new Conversation(
                new Contact("Javache", InetAddress.getByName("thinkjavache.be")), true);
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
                new Contact("Javache", InetAddress.getByName("ugent.be")), true);
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
        // clear the converstion created in setUp()
        EventPool.getAppPool().removeListener(conversation);

        // get the local app-pool and let it start
        EventPool localPool = EventPool.getAppPool();
        Thread.sleep(100);

        // create a remote eventpool and connect it to the local one
        EventPool remotePool = new EventPool();
        Connection remoteConnection = new Connection(InetAddress.getLocalHost(), remotePool, null);
        Thread.sleep(100);

        // create a contact out of the local user (as seen from the other side)
        Account localAccount = ChatApplication.getInstance().getAccount();
        Contact localContact = new Contact(localAccount.getUsername(), InetAddress.getLocalHost());
        Contact remoteContact = new Contact("Jaspervdj", InetAddress.getLocalHost());

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
        remotePool.addListener(remoteConversation, new ConversationEventFilter(remoteConversation));
        // remove remoteListener from localPool
        localPool.removeListener(remoteConversation);

        // now everything is setup, and a user can type a message
        ChatMessage chatMessage = new ChatMessage(localContact.getUsername(), "Dag Javache, jij jij remoteUser!");
        ConversationEvent localEvent = new NewChatMessageEvent(localConversation, chatMessage);

        // raise the event on the local pool, should get sent to remotePool too
        localPool.raiseNetworkEvent(localEvent);
        Thread.sleep(100);

        // let's check the results!
        assertEquals(1, remoteConversation.getChatMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", remoteConversation.getChatMessages(1)[0].getText());

        assertEquals(1, localConversation.getChatMessages(10).length);
        assertEquals("Dag Javache, jij jij remoteUser!", localConversation.getChatMessages(1)[0].getText());
    }
}
