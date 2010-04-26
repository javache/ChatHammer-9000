package ch9k.chat.gui;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.gui.views.ContactListMouseListener;
import ch9k.core.ChatApplication;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Show the list of contacts
 * @author Jens Panneel
 */
public class ContactListView extends JPanel {
    private ContactList contacts;
    private JList list;
    
    public ContactListView() {
        contacts = ChatApplication.getInstance().getAccount().getContactList();
        list = new JList(contacts);
        list.setBackground(getBackground());

        setLayout(new BorderLayout());
        add(list, BorderLayout.NORTH);
        
        list.setCellRenderer(new ContactListCellRenderer());
        list.addMouseListener(new ContactListMouseListener(list));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                list.clearSelection();
            }
        });
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
            setFont(getFont().deriveFont(14f));
            setBorder(BorderFactory.createEmptyBorder(5, 9, 5, 0));
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
