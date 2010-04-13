package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jens Panneel
 */
public class ContactListCellRenderer extends JLabel implements ListCellRenderer{

    final static ImageIcon onlineIcon = new ImageIcon("online.png");
    final static ImageIcon awayIcon = new ImageIcon("away.png");
    final static ImageIcon offlineIcon = new ImageIcon("offline.png");
    final static ImageIcon blockedIcon = new ImageIcon("blocked.png");

    @Override
    public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
        Contact contact = (Contact) object;
        // use icons !
        String online;
        if(contact.isOnline()) {
            online = "Online";
        } else {
            online = "Offline";
        }
        String label = "[" + online + "] " + contact.getUsername();
        setText(label);
        if (isSelected) {
         setBackground(list.getSelectionBackground());
         setForeground(list.getSelectionForeground());
        } else {
         setBackground(list.getBackground());
         setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
