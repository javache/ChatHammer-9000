package ch9k.chat.gui;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.gui.views.ContactListPopupListener;
import ch9k.core.ChatApplication;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Show the list of contacts
 * @author Jens Panneel
 */
public class ContactListView extends JList {
    private ContactList contacts;
    
    public ContactListView() {
        contacts = ChatApplication.getInstance().getAccount().getContactList();
        setModel(contacts);
        
        setCellRenderer(new ContactListCellRenderer());
        addMouseListener(new ContactListPopupListener(this));
    }

    /**
     * Renders a contact
     */
    public class ContactListCellRenderer extends JLabel implements ListCellRenderer {
        private StatusIcon icon;

        public ContactListCellRenderer() {
            setOpaque(true);

            icon = new StatusIcon(14, true);
            setIcon(icon);
            setFont(getFont().deriveFont(13f));
            setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 0));
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object object,
                int index, boolean isSelected, boolean cellHasFocus) {

            Contact contact = (Contact) object;

            setText(contact.getUsername());
            icon.setOnline(contact.isOnline());

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
}
