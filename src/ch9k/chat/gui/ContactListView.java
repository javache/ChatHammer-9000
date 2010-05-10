package ch9k.chat.gui;

import ch9k.chat.AddContactController;
import ch9k.chat.BlockedContactFilter;
import ch9k.chat.Contact;
import ch9k.chat.ContactFilter;
import ch9k.chat.ContactList;
import ch9k.chat.FilteredContactList;
import ch9k.chat.IgnoredContactFilter;
import ch9k.chat.gui.components.SearchField;
import ch9k.chat.gui.components.StatusIcon;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.SystemColor;
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
    private SearchField searchField;
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
        searchField = new SearchField(contactList);
        
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

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(searchField, BorderLayout.CENTER);
        northContainer.add(addButton, BorderLayout.EAST);
        northContainer.setBorder(BorderFactory.createEmptyBorder(5, 6, 5, 3));
        add(northContainer, BorderLayout.NORTH);
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
    public class ContactListCellRenderer extends JPanel implements ListCellRenderer {
        private JLabel username;
        private JLabel status;
        private StatusIcon icon;

        public ContactListCellRenderer() {
            super(new FlowLayout(FlowLayout.LEFT));
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 0));

            username = new JLabel();
            icon = new StatusIcon(13, true);
            username.setIcon(icon);
            username.setFont(getFont().deriveFont(13f));
            add(username);

            status = new JLabel();
            status.setFont(getFont().deriveFont(12f));
            status.setForeground(SystemColor.textInactiveText);
            status.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 0));
            add(status);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object object,
                int index, boolean isSelected, boolean cellHasFocus) {
            Contact contact = (Contact) object;

            username.setText(contact.getUsername());
            status.setText(contact.getStatus());
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
