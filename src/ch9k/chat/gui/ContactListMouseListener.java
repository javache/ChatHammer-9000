package ch9k.chat.gui;

import ch9k.chat.Contact;
import ch9k.chat.gui.actions.ContactBlockAction;
import ch9k.chat.gui.actions.ContactDeleteAction;
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
        act(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        act(e);

        // double click
        if(!e.isPopupTrigger() && e.getClickCount() == 2) {
            Contact contact = (Contact)list.getSelectedValue();
            Action action = new StartConversationAction(contact);
            if(action.isEnabled()) {
                action.actionPerformed(null);
            }
        }
    }

    private void act(MouseEvent e) {        
        // right click
        if(e.isPopupTrigger()) {
            list.setSelectedIndex(list.locationToIndex(e.getPoint()));
            Contact contact = (Contact)list.getSelectedValue();
            JPopupMenu contactListPopupMenu = new ContactListPopupMenu(contact);
            contactListPopupMenu.show(list, e.getX(), e.getY());
        }
    }

    private class ContactListPopupMenu extends JPopupMenu {
        public ContactListPopupMenu(Contact contact) {
            add(new StartConversationAction(contact));
            add(new ContactBlockAction(contact));
            add(new ContactDeleteAction(contact));
        }
    }
}
