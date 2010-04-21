package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import ch9k.chat.gui.actions.ContactBlockAction;
import ch9k.chat.gui.actions.ContactUnblockAction;
import ch9k.chat.gui.actions.StartConversationAction;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JPopupMenu;

/**
 *
 * @author Jens Panneel
 */

    public class ContactListPopupListener extends MouseAdapter {
    private JList list;

    public ContactListPopupListener(JList list) {
        this.list = list;
    }



    @Override
    public void mouseReleased(MouseEvent e) {
        act(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        act(e);
    }

    private void act(MouseEvent e) {
        if (e.isPopupTrigger()) {
            list.setSelectedIndex(
            list.locationToIndex(new Point(e.getX(), e.getY())));
            Contact contact = (Contact)list.getSelectedValue();
            JPopupMenu contactListPopupMenu = new ContactListPopupMenu(contact);
            contactListPopupMenu.show(list, e.getX(), e.getY());
        }
    }


    private class ContactListPopupMenu extends JPopupMenu {
        public ContactListPopupMenu(Contact contact) {
            super();
            if(contact.isBlocked()){
                add(new ContactUnblockAction(contact));
            } else {
                add(new ContactBlockAction(contact));
            }
            if(contact.isOnline()) {
                add(new StartConversationAction(contact));
            }
        }
    }
}
