package ch9k.core.gui;

import ch9k.chat.gui.ContactListView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private ContactListView contactList;
    private AccountPanel account;

    /**
     * Create a new window
     */
    public ApplicationWindow() {
        super("ChatHammer 9000");
        // @TODO: check how this works out on mac (where windows can be 'hidden'
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
        System.setProperty("apple.laf.useScreenMenuBar", "true");
    }

    /**
     * Init the 'real' application view, after login has completed
     */
    public void initApplicationView() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        account = new AccountPanel();
        panel.add(account, BorderLayout.NORTH);

        contactList = new ContactListView();
        contactList.setBackground(getBackground());
        panel.add(contactList, BorderLayout.CENTER);

        statusBar = new JLabel();
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(panel);
        setVisible(true);
        repaint();
    }

    public void setStatus(final String status, boolean autoFade) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                statusBar.setText(status);
            }
        });

        if(autoFade) {
            new Thread(new Runnable() {
                public void run() {
                    setStatus("", false);
                }
            }).start();
        }
    }
}
