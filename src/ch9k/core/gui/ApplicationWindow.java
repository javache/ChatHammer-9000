package ch9k.core.gui;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.chat.gui.views.ContactListCellRenderer;
import ch9k.chat.gui.views.ContactListPopupListener;
import ch9k.core.ChatApplication;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;

/**
 * Main application window
 * @author Pieter De Baets
 */
public class ApplicationWindow extends JFrame {
    /**
     * Logger.
     */
    private static final Logger logger =
            Logger.getLogger(ApplicationWindow.class);

    private JPanel panel;
    private JLabel statusBar;
    private JList contactList;
    private AccountPanel account;

    /**
     * Create a new window
     */
    public ApplicationWindow() {
        super("ChatHammer 9000");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(300, 520));
        setMinimumSize(new Dimension(300, 200));
        setSize(getPreferredSize());
        setLocationByPlatform(true);

        initSettings();
    }

    private void initSettings() {
        // use a native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.info("Unable to load native look & feel");
        }

        // set the menu bar on top of the screen on mac
        System.setProperty ("apple.laf.useScreenMenuBar", "true");
    }

    /**
     * Init the 'real' application view, after login has completed
     */
    public void initApplicationView() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        account = new AccountPanel();
        panel.add(account, BorderLayout.NORTH);

        //for testing purpose
        ContactList contacts = ChatApplication.getInstance()
                .getAccount().getContactList();

        try {
            Contact contact1 = new Contact("JPanneel",
                    InetAddress.getByName("google.be"), false);
            contacts.addContact(contact1);
            Contact contact2 = new Contact("Javache",
                    InetAddress.getByName("google.be"), false);
            contacts.addContact(contact2);
        } catch (UnknownHostException ex) {
            // just for testing
        }
        
        contactList = new JList(contacts);
        contactList.setCellRenderer(new ContactListCellRenderer());
        contactList.addMouseListener(new ContactListPopupListener(contactList));
        panel.add(contactList, BorderLayout.CENTER);

        statusBar = new JLabel();
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(panel);
        repaint();
    }

    public void setStatus(String status) {
        statusBar.setText(status);
    }
}
