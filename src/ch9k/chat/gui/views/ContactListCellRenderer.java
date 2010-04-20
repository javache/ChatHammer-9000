package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import ch9k.chat.gui.StatusIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jens Panneel
 */
public class ContactListCellRenderer extends JPanel implements ListCellRenderer {

    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JToggleButton blockButton;

    public ContactListCellRenderer() {
        super();
        setLayout(new BorderLayout());
        setOpaque(true);

        contactLabel = new JLabel();
        contactLabel.setIcon(new StatusIcon(20, false));
        contactLabel.setFont(contactLabel.getFont().deriveFont(15f));
        contactLabel.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 0));
        blockButton = new JToggleButton();
        deleteButton = new JButton();

        this.add(contactLabel,BorderLayout.WEST);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object object,
            int index, boolean isSelected, boolean cellHasFocus) {

        Contact contact = (Contact) object;
        contactLabel.setText(contact.getUsername());

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
