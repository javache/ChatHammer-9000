package ch9k.chat.gui;

import ch9k.chat.gui.components.StatusIcon;
import ch9k.chat.AddContactController;
import ch9k.chat.BlockedContactFilter;
import ch9k.chat.Contact;
import ch9k.chat.ContactFilter;
import ch9k.chat.ContactList;
import ch9k.chat.FilteredContactList;
import ch9k.chat.IgnoredContactFilter;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
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
    private FilteredContactList contactList;
    
    public ContactListView() {
        super(new BorderLayout());
        contacts = ChatApplication.getInstance().getAccount().getContactList();

        initComponents();
        initLayout();
    }

    private void initComponents() {
        addButton = new JButton(new AddContactAction(this));
        contactList = new FilteredContactList(contacts);
        
        list = new JList(contactList);
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

    public void initMenu(JMenu parentMenu) {
        // add buttons for filters
        parentMenu.add(new FilterMenuItem(I18n.get("ch9k.chat", "show_blocked"),
                new BlockedContactFilter(), true));
        parentMenu.add(new FilterMenuItem(I18n.get("ch9k.chat", "show_ignored"),
                new IgnoredContactFilter(), false));
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
            setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 0));
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

    public static class AddContactAction extends AbstractAction {
        private Frame parent;

        public AddContactAction(Component component) {
            super(I18n.get("ch9k.chat", "add_contact"));
            parent = (Frame)SwingUtilities.getRoot(component);
        }

        public void actionPerformed(ActionEvent e) {
            new AddContactController(parent);
        }
    }

    public class FilterMenuItem extends JCheckBoxMenuItem implements ActionListener {
        private ContactFilter filter;

        public FilterMenuItem(String name, ContactFilter filter, boolean startValue) {
            super(name);
            this.filter = filter;
            addActionListener(this);

            // start enabled, TODO: load default from settings
            setSelected(startValue);
            actionPerformed(null);
        }

        public void actionPerformed(ActionEvent e) {
            if(isSelected()) {
                contactList.removeFilter(filter);
            } else {
                contactList.addFilter(filter);
            }
        }
    }
}
