package ch9k.core;

import ch9k.core.gui.ApplicationWindow;
import ch9k.core.gui.RegistrationPanel;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.net.InetAddress;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Performs registration of a user
 * @author Pieter De Baets
 */
public class RegistrationController {
    private LoginController loginController;
    private RegistrationPanel view;

    public RegistrationController(LoginController loginController, ApplicationWindow window) {
        this.loginController = loginController;

        // create dialog
        JDialog dialog = new JDialog(window,
                I18n.get("ch9k.core", "register_account"),
                ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(window);
        dialog.setResizable(false);

        // create view
        view = new RegistrationPanel(this, dialog);
        
        // fetch the ip to show in a different thread
        new Thread(new Runnable() {
            public void run() {
                InetAddress[] addresses = Account.getInetAddresses();

                final StringBuilder ipText = new StringBuilder();
                for (int i = 0; i < addresses.length; i++) {
                    if(i > 0) {
                        ipText.append(", ");
                    }
                    ipText.append(addresses[i].getHostAddress());
                }

                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        view.setIp(ipText.toString());
                    }
                });
            }
        }).start();

        // show the dialog (will block, since this is a modal window)
        dialog.pack();
        Dimension dimension = dialog.getSize();
        dialog.setSize(dimension.width, dimension.height + 50);
        dialog.setVisible(true);
    }

    /**
     * Validate a given set of inputs
     * @param username
     * @param password
     * @param repeatedPassword
     * @return success
     */
    public boolean validate(String username, String password, String repeatedPassword) {
        // do some validation
        if(username.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()) {
            view.setError(I18n.get("ch9k.core", "error_fill_all_fields"));
        } else if(!password.equals(repeatedPassword)) {
            view.setError(I18n.get("ch9k.core", "error_passwords_not_equal"));
        } else if(username.length() < 6) {
            view.setError(I18n.get("ch9k.core", "error_username_too_short"));
        } else if(password.length() < 6) {
            view.setError(I18n.get("ch9k.core", "error_password_too_short"));
        } else {
            // perform registration
            // todo: check if user already exists
            view.setError(null);
            loginController.register(username, password);
            return true;
        }
        return false;
    }
}
