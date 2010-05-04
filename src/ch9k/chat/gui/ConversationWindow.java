package ch9k.chat.gui;

import ch9k.chat.gui.components.MessageEditor;
import ch9k.chat.Conversation;
import ch9k.chat.event.CloseConversationEvent;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.RequestPluginContainerEvent;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.core.gui.WindowMenu;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;

/**
 * Shows a conversation
 * @author Pieter De Baets
 */
public class ConversationWindow extends JFrame {
    /**
     * The shown conversation
     */
    private Conversation conversation;

    /**
     * Window is rendered as a split pane
     */
    private JSplitPane splitPane;

    /**
     * Panel upon which a plugin is rendered
     */
    private JPanel pluginPanel;

    /**
     * Show all messages in the conversation
     */
    private ConversationListView conversationView;

    /**
     * Inputbox for new chatmessages
     */
    private MessageEditor editor;

    /**
     * Construct a new ConversationWindow
     * Must be called from the Swing-thread
     * @param conversation
     */
    public ConversationWindow(Conversation conversation) {
        super(conversation.getContact().getUsername() + " @ " + conversation.getContact().getIp().getCanonicalHostName());
        this.conversation = conversation;
    }

    public void init() {
        EventPool.getAppPool().addListener(new EventListener() {
            @Override
            public void handleEvent(Event event) {
                RequestPluginContainerEvent requestEvent = (RequestPluginContainerEvent)event;
                EventPool.getAppPool().raiseEvent(new RequestedPluginContainerEvent(
                    conversation, pluginPanel));
            }
        }, new ConversationEventFilter(RequestPluginContainerEvent.class, conversation));

        // listen for close-events
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                EventPool.getAppPool().raiseNetworkEvent(
                        new CloseConversationEvent(conversation));
            }
        });

        initComponents();

        // Add a menu bar, containing a menu in which different 
        // plugins can be selected
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new PluginMenu(conversation));
        menuBar.add(new WindowMenu(this));
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void initComponents() {
        pluginPanel = new JPanel();
        conversationView = new ConversationListView(conversation);
        editor = new MessageEditor(conversation);

        JPanel conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.add(conversationView, BorderLayout.CENTER);
        conversationPanel.add(editor, BorderLayout.SOUTH);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                pluginPanel, conversationPanel);
        splitPane.setSize(new Dimension(900, 600));

        splitPane.setDividerLocation(0.55);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        setSize(splitPane.getSize());
        setMinimumSize(new Dimension(600, 400));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
