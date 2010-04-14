package ch9k.chat.gui.actions;

import ch9k.chat.Contact;
import ch9k.chat.events.NewConversationEvent;
import ch9k.eventpool.EventPool;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Jens Panneel
 */
public class StartConversationAction extends AbstractAction {
    private Contact contact;

    public StartConversationAction(Contact contact) {
        super("send chatmessage");
        this.contact = contact;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewConversationEvent event = new NewConversationEvent(contact);
        EventPool.getAppPool().raiseEvent(event);
    }
}
