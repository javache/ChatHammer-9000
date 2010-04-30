package ch9k.chat.gui;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.RequestPluginContainerEvent;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
    public ConversationWindow(final Conversation conversation) {
        super(conversation.getContact().getUsername() + " @ " + conversation.getContact().getIp().getCanonicalHostName());
        this.conversation = conversation;

        EventPool.getAppPool().addListener(new EventListener() {
            @Override
            public void handleEvent(Event event) {
                // TODO: make this a normal method call?
                RequestPluginContainerEvent requestEvent = (RequestPluginContainerEvent)event;
                EventPool.getAppPool().raiseEvent(new RequestedPluginContainerEvent(
                    conversation, pluginPanel));
            }
        }, new ConversationEventFilter(RequestPluginContainerEvent.class, conversation));

        initComponents();
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
        splitPane.setSize(new Dimension(800, 600));

        splitPane.setDividerLocation(0.55);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        setSize(splitPane.getSize());
        setMinimumSize(new Dimension(600, 400));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Mark the conversation as closed
     * @param closed
     */
    public void markAsClosed(boolean closed) {
        if(closed) {
            conversationView.notifyClosed();
        }
    }
}
