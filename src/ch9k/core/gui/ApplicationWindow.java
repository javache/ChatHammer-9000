package ch9k.core.gui;

import ch9k.chat.gui.ContactListView;
import ch9k.core.ChatApplication;
import ch9k.core.event.AccountLogoffEvent;
import ch9k.core.settings.ProxyPrefPane;
import ch9k.core.settings.event.PreferencePaneEvent;
import ch9k.core.settings.gui.PreferencesFrame;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;

/**
 * Main application window
 * @author Pieter De Baets
 */
public class ApplicationWindow extends JFrame implements EventListener {
    /**
     * Logger.
     */
    private static final Logger logger =
            Logger.getLogger(ApplicationWindow.class);

    private JPanel panel;
    private JLabel statusBar;
    private ContactListView contactList;
    private AccountPanel account;

    private PreferencesFrame prefFrame;

    /**
     * Create a new window
     */
    public ApplicationWindow() {
        super("ChatHammer 9000");
        // @TODO: check how this works out on mac (where windows can be 'hidden'
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        EventPool.getAppPool().addListener(this, new EventFilter(AccountLogoffEvent.class));

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

        prefFrame = new PreferencesFrame();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(ChatApplication.getInstance().getAccount().getUsername());
        menu.add(new AbstractAction("show Preferences"){
            @Override
            public void actionPerformed(ActionEvent e) {
                prefFrame.setVisible(! prefFrame.isVisible());
            }
        });
        menuBar.add(menu);
        setJMenuBar(menuBar);

        EventPool.getAppPool().raiseEvent(new PreferencePaneEvent("proxy", new ProxyPrefPane()));

        setContentPane(panel);
        setVisible(true);
        repaint();
    }

    private Queue<String> statusQueue = new LinkedList<String>();

    public synchronized void setStatus(String status) {
        System.out.println("NEW STATUS: " + status);
        if(status != null) {
            statusQueue.add(status);
        }

        if(statusQueue.size() == 1) {
            refreshStatus();
        }
    }

    private synchronized void refreshStatus() {
        if(!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    refreshStatus();
                }
            });
            return;
        }

        String status = null;
        if(statusBar != null) {
            status = statusQueue.poll();
            System.out.println("SHOWING STATUS: " + status);
            statusBar.setText(status);
        }

        if(status != null || statusQueue.size() > 0) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException ex) {}

                    refreshStatus();
                }
            }).start();
        }
    }

    @Override
    public void handleEvent(Event event) {
        getJMenuBar().remove(0);
        prefFrame.close();
    }
}
