package ch9k.chat.gui.views;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.RequestPluginPanelEvent;
import ch9k.chat.events.RequestedPluginPanelEvent;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Jens Panneel
 */
public class ConversationView extends JFrame implements EventListener{
    private Conversation conversation;

    public ConversationView(Conversation conversation) {
        super("Conversation with " + conversation.getContact().getUsername());
        this.conversation = conversation;
        EventPool.getAppPool().addListener(this, 
                new ConversationEventFilter(RequestPluginPanelEvent.class,
                conversation));

        this.setSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //setting the layout of the contentpane
        //what should it contain?
        //chatmessagelist
        //inputpanel
        //panels for plugins?
        //this.getContentPane().setLayout(new BorderLayout());
        //make components and add them to this.getContentPane()
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(new ChatMessageListView(conversation));
        this.getContentPane().add(scrollPane);
    }

    @Override
    public void handleEvent(Event event) {
        JPanel pluginPanel = new JPanel();
        // should this event be broadcasted trough network?
        EventPool.getAppPool().raiseEvent(
                new RequestedPluginPanelEvent(conversation, pluginPanel));
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Account account = ChatApplication.getInstance().getAccount();
        ContactList contactList = account.getContactList();

        Contact contact1 = new Contact("JPanneel",
                InetAddress.getByName("google.be"), false);
        contactList.addContact(contact1);

        ConversationManager conversationManager =
                ChatApplication.getInstance().getConversationManager();
        final Conversation conversation1 =
                conversationManager.startConversation(contact1, true);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConversationView(conversation1);
            }
        });

        ChatMessage[] messages = new ChatMessage[] {
            new ChatMessage("JPanneel", "Hey!"),
            new ChatMessage("Wendy", "O dag lieverd"),
            new ChatMessage("JPanneel", "Hoe gaat het met de overkant?"),
            new ChatMessage("Wendy", "Goed, maar ik mis je wel!"),
            new ChatMessage("JPanneel", "Ik weet het :)"),
            new ChatMessage("Wendy", "Doei!")
        };

        for (int i = 0; i < 5; i++) {
            conversation1.addMessage(messages[i]);
        }
    }
}
