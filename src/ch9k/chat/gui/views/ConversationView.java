package ch9k.chat.gui.views;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationManager;
import ch9k.chat.events.CloseConversationEvent;
import ch9k.chat.events.ContactOfflineEvent;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.chat.events.NewConversationEvent;
import ch9k.chat.events.RequestPluginPanelEvent;
import ch9k.chat.events.RequestedPluginPanelEvent;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Jens Panneel
 */
public class ConversationView extends JFrame implements EventListener, ChangeListener{

    private Conversation conversation;

    private JPanel pluginPanel;
    private JPanel conversationPanel;

    public ConversationView(Conversation conversation) {
        super("Conversation with " + conversation.getContact().getUsername());
        this.conversation = conversation;
        EventPool.getAppPool().addListener(this, 
                new ConversationEventFilter(conversation));

        this.setSize(new Dimension(1200, 1024));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //splits het contentpane in links (plugin) en rechts (vonversation)
        this.getContentPane().setLayout(new BorderLayout());
        pluginPanel = new JPanel();
        pluginPanel.setPreferredSize(new Dimension(700, 1024));
        pluginPanel.setBackground(Color.red);
        this.getContentPane().add(pluginPanel, BorderLayout.CENTER);

        conversationPanel = new ConversationPanel(conversation);
        conversationPanel.setPreferredSize(new Dimension(500, 1024));
        conversationPanel.setBackground(Color.blue);
        this.getContentPane().add(conversationPanel, BorderLayout.EAST);
        
        this.setVisible(true);
    }

    @Override
    public void handleEvent(Event event) {
        if(event instanceof RequestPluginPanelEvent) {
            // should this event be broadcasted trough network?
            // could make this a plugineven..?
            EventPool.getAppPool().raiseEvent(
                    new RequestedPluginPanelEvent(conversation, pluginPanel));
        }

        /*
        if(event instanceof CloseConversationEvent) {

        }

        if(event instanceof NewConversationEvent) {
            //this.requestFocus();

        }

        if(event instanceof NewChatMessageEvent) {
            //this.requestFocus();
        }

        if(event instanceof ContactOfflineEvent) {

        }
        */
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Account account = ChatApplication.getInstance().getAccount();
        ContactList contactList = account.getContactList();

        Contact contact1 = new Contact("JPanneel",
                InetAddress.getByName("google.be"), false);
        contactList.addContact(contact1);

        Contact contact2 = new Contact("Javache",
                InetAddress.getByName("thinkJavache.be"), false);
        contactList.addContact(contact2);

        ConversationManager conversationManager =
                ChatApplication.getInstance().getConversationManager();
        final Conversation conversation1 =
                conversationManager.startConversation(contact1, true);
        final Conversation conversation2 =
                conversationManager.startConversation(contact2, true);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConversationView(conversation2);
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

        Thread.sleep(6000);
        conversation2.addMessage(new ChatMessage("me", "dag javacke"));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
