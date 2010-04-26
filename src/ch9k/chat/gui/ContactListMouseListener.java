package ch9k.chat.gui;

import ch9k.chat.Contact;
import ch9k.chat.gui.actions.ContactBlockAction;
import ch9k.chat.gui.actions.StartConversationAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JPopupMenu;

/**
 * Popuplistener for contactlist
 * @author Jens Panneel
 */
public class ContactListMouseListener extends MouseAdapter {
    private JList list;

    public ContactListMouseListener(JList list) {
        this.list = list;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.isPopupTrigger()) {
            list.setSelectedIndex(list.locationToIndex(e.getPoint()));
            Contact contact = (Contact)list.getSelectedValue();
            JPopupMenu contactListPopupMenu = new ContactListPopupMenu(contact);
            contactListPopupMenu.show(list, e.getX(), e.getY());
        }

        if(!e.isPopupTrigger() && e.getClickCount() == 2) {
            Contact contact = (Contact)list.getSelectedValue();
            Action action = new StartConversationAction(contact);
            if(action.isEnabled()) {
                action.actionPerformed(null);
            }
        }
    }

    private class ContactListPopupMenu extends JPopupMenu {
        public ContactListPopupMenu(Contact contact) {
            add(new StartConversationAction(contact));
            add(new ContactBlockAction(contact));
        }
    }
}
