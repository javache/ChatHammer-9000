package ch9k.chat;

import ch9k.chat.gui.AddContactPanel;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.Frame;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Shows a dialog to enter
 * @author Pieter De Baets
 */
public class AddContactController {
    private AddContactPanel view;

    public AddContactController(Frame window) {
        JDialog dialog = new JDialog(window,
                I18n.get("ch9k.chat", "add_contact"), true);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(window);
        dialog.setResizable(false);

        view = new AddContactPanel(this, dialog);
        dialog.setVisible(true);
    }

   public boolean addContact(String username, String inetAddress) {
        InetAddress ip = null;
        boolean hasErrors = false;

        ContactList list = ChatApplication.getInstance().
            getAccount().getContactList();

        // do some validation
        if(username.isEmpty() || inetAddress.isEmpty()) {
            view.setError(I18n.get("ch9k.core", "error_fill_all_fields"));
            hasErrors = true;
        }

        try {
            ip = InetAddress.getByName(inetAddress);
            if(InetAddress.getLocalHost().equals(ip)) {
                System.err.println("Own ip fagot");
                view.setError(I18n.get("ch9k.chat", "error_own_ip"));
                hasErrors = true;
            }
        } catch(UnknownHostException ex) {
            view.setError(I18n.get("ch9k.chat", "error_invalid_ip"));
            hasErrors = true;
        }


        if(list.getContact(ip, username) != null) {
            view.setError(I18n.get("ch9k.chat", "error_contact_already_added"));
            hasErrors = true;
        }

        if(!hasErrors) {
            list.addContact(new Contact(username, ip), true);
            return true;
        } else {
            return false;
        }
   }
}
