package ch9k.core;

import ch9k.core.event.AccountLogoffEvent;
import ch9k.core.event.AccountLoginEvent;
import ch9k.eventpool.StatusEvent;
import ch9k.chat.ConversationManager;
import ch9k.configuration.Configuration;
import ch9k.core.gui.ApplicationWindow;
import ch9k.core.settings.Settings;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.WarningEvent;
import ch9k.plugins.PluginManager;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The main application, OMG!
 *
 * @author Bruno
 * @author Pieter De baets
 */
public class ChatApplication implements EventListener {
    public static ChatApplication getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* Helper-class for singleton, aka Bill Pugh's method */
    private static class SingletonHolder {
         private static final ChatApplication INSTANCE = new ChatApplication();
    }

    private ApplicationWindow appWindow;
    private Configuration configuration;
    private ConversationManager conversationManager;

    public static void main(String[] args) {
        ChatApplication app = ChatApplication.getInstance();
        app.start(args);
    }

    private ChatApplication() {
        conversationManager = new ConversationManager();
        appWindow = new ApplicationWindow();

        appWindow.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                exit();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                exit();
            }
        });

        EventPool.getAppPool().addListener(this,
                new EventFilter(StatusEvent.class));
    }

    private void start(String[] args) {
        Configuration newConfiguration = null;

        // perform auto login?
        if(args!= null && args.length == 2) {
            newConfiguration = new Configuration(args[0]);
            Account account = newConfiguration.getAccount();
            if(account == null || !account.authenticate(args[1])) {
                newConfiguration = null;
                System.err.println("Failed to login with provided credentials");
            }
        }

        if(newConfiguration == null) {
            // show login dialog
            LoginController loginController = new LoginController();
            newConfiguration = loginController.run(appWindow);
            if(newConfiguration == null) {
                // user closed window
                System.exit(0);
            }
        }
        configuration = newConfiguration;
        configuration.save();

        appWindow.initApplicationView();
        appWindow.setStatus(I18n.get("ch9k.core", "booting"));
        EventPool.getAppPool().raiseEvent(new AccountLoginEvent());

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ex) {}

                InetAddress localhost = null;
                try {
                    localhost = InetAddress.getLocalHost();
                } catch(UnknownHostException ex) {}

                // send events here for testing
                // they will be executed 2 seconds after app started
            }
        }).start();
    }

    public void exit() {
        if(configuration != null) {
            logoff(false);
        }
        EventPool.getAppPool().close();
    }

    /**
     * Logoff the current account
     * @param showLogin
     */
    public void logoff(boolean showLogin) {
        configuration.save();
        EventPool.getAppPool().raiseEvent(new AccountLogoffEvent());

        if(showLogin) {
            new Thread(new Runnable() {
                public void run() {
                    start(null);
                }
            }).start();
        }
    }

    /**
     * Get the currently logged in account, may return null if authentication
     * is not performed yet
     * @return account
     */
    public Account getAccount() {
        if(configuration != null) {
            return configuration.getAccount();
        } else {
            return null;
        }
    }

    /**
     * Get the current user's settings, may return null if authentication
     * is not performed yet
     * @return
     */
    public Settings getSettings() {
        if(configuration != null) {
            return configuration.getSettings();
        } else {
            return null;
        }
    }

    /**
     * Get the plugin manager.
     * @return The PluginManager.
     */
    public PluginManager getPluginManager() {
        if(configuration != null) {
            return configuration.getPluginManager();
        } else {
            return null;
        }
    }

    /**
     * Get the main application window
     * @return window
     */
    public ApplicationWindow getWindow() {
        return appWindow;
    }

    /**
     * Get the conversation manager
     * @return conversationManager
     */
    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    @Override
    public void handleEvent(Event event) {
        StatusEvent statusEvent = (StatusEvent)event;
        String message = statusEvent.getMessage();

        if(statusEvent instanceof WarningEvent) {
            message = "<html><font color=\"red\">" + message;
        }
        appWindow.setStatus(message);
    }

    /**
     * Perform a fake login
     * (to be used for testing purposes only!)
     */
    public void performTestLogin() {
        configuration = new Configuration("CH9K");
        configuration.setAccount(new Account("CH9K", "password"));
    }
}
