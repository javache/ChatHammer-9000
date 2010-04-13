package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jens Panneel
 */
public class ContactListCellRenderer extends JLabel implements ListCellRenderer {
    public ContactListCellRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object object,
            int index, boolean isSelected, boolean cellHasFocus) {
        Contact contact = (Contact) object;
        
        String online;
        if(contact.isOnline()) {
            online = "online";
        } else {
            online = "offline";
        }
        String blocked = "";
        if(contact.isBlocked()) {
            blocked = "blocked";
        }
        setText("[" + online + "] "  + "[" + blocked + "] "+ contact.getUsername());
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
