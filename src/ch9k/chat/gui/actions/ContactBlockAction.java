package ch9k.chat.gui.actions;

import ch9k.chat.Contact;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Jens Panneel
 */
public class ContactBlockAction extends AbstractAction {
    private Contact contact;

    public ContactBlockAction(Contact contact) {
        super("block");
        this.contact = contact;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        contact.setBlocked(true);
    }

}
