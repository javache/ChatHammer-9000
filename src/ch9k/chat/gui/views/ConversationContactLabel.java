package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Jens Panneel
 */
public class ConversationContactLabel extends JLabel implements ChangeListener{
    private Contact contact;

    public ConversationContactLabel(Contact contact) {
        super();
        this.contact = contact;
        contact.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.repaint();
    }
}
