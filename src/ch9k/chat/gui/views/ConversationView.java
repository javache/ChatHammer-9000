package ch9k.chat.gui.views;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.RequestPluginPanelEvent;
import ch9k.chat.events.RequestedPluginPanelEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Jens Panneel
 */
public class ConversationView implements EventListener, ChangeListener{
    private Conversation conversation;

    public ConversationView(Conversation conversation) {
        this.conversation = conversation;
        EventPool.getAppPool().addListener(this, new ConversationEventFilter(RequestPluginPanelEvent.class, conversation));
        conversation.addChangeListener(this);
    }

    @Override
    public void handleEvent(Event event) {
        JPanel pluginPanel = new JPanel();
        // should this event be broadcasted trough network?
        EventPool.getAppPool().raiseEvent(new RequestedPluginPanelEvent(conversation, pluginPanel));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // repaint that messagelist!
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
