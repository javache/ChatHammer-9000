package ch9k.chat.gui.actions;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Pieter De Baets
 */
public class ContactDeleteAction extends AbstractAction {
    private Contact contact;

    public ContactDeleteAction(Contact contact) {
        super("delete");
        this.contact = contact;

        putValue(NAME, getName());
    }

    private String getName() {
        return I18n.get("ch9k.chat", "delete");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ContactList list = ChatApplication.getInstance().getAccount().getContactList();
        list.removeContact(contact);
    }
}
