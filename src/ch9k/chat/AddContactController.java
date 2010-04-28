package ch9k.chat;

import ch9k.chat.gui.AddContactPanel;
import ch9k.core.I18n;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Shows a dialog to enter
 * @author Pieter De Baets
 */
public class AddContactController {
    private AddContactPanel view;

    public AddContactController(Window window) {
        JDialog dialog = new JDialog(window,
                I18n.get("ch9k.chat", "add_contact"),
                ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(window);
        dialog.setResizable(false);

        view = new AddContactPanel(this, dialog);
        
        dialog.pack();
        dialog.setVisible(true);
    }

   public void addContact() {
       
   }
}
