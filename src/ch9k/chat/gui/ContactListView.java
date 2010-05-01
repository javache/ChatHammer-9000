package ch9k.chat.gui;

import ch9k.chat.gui.components.StatusIcon;
import ch9k.chat.AddContactController;
import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

/**
 * Show the list of contacts
 * @author Jens Panneel
 */
public class ContactListView extends JPanel {
    private ContactList contacts;
    private JList list;
    private JButton addButton;
    
    public ContactListView() {
        super(new BorderLayout());
        contacts = ChatApplication.getInstance().getAccount().getContactList();

        initComponents();
        initLayout();
    }

    private void initComponents() {
        addButton = new JButton(new AbstractAction(I18n.get("ch9k.chat", "add_contact")) {
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(ContactListView.this);
                new AddContactController(window);
            }
        });
        
        list = new JList(contacts);
        list.setBackground(getBackground());
        list.setCellRenderer(new ContactListCellRenderer());
        list.addMouseListener(new ContactListMouseListener(list));
    }

    private void initLayout() {
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                list.clearSelection();
            }
        });
        listContainer.add(list, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setBackground(getBackground());
        add(scrollPane, BorderLayout.CENTER);
        
        add(addButton, BorderLayout.NORTH);
    }

    /**
     * Renders a contact
     */
    public class ContactListCellRenderer extends JLabel implements ListCellRenderer {
        private StatusIcon icon;

        public ContactListCellRenderer() {
            setOpaque(true);

            icon = new StatusIcon(13, true);
            setIcon(icon);
            setFont(getFont().deriveFont(13f));
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
