package ch9k.chat.gui.actions;

import ch9k.chat.Contact;
import ch9k.core.I18n;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Block a contact action
 * @author Jens Panneel
 */
public class ContactBlockAction extends AbstractAction {
    private Contact contact;

    public ContactBlockAction(Contact contact) {
        super("block");
        this.contact = contact;

        putValue(NAME, getName());
    }

    private String getName() {
        if(contact.isBlocked()) {
            return I18n.get("ch9k.chat", "unblock");
        } else {
            return I18n.get("ch9k.chat", "block");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        contact.setBlocked(!contact.isBlocked());
    }
}
