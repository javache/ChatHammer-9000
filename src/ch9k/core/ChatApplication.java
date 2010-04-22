package ch9k.core;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.core.gui.ApplicationWindow;
import ch9k.chat.ConversationManager;
import ch9k.configuration.Configuration;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The main application, OMG!
 *
 * @author Bruno
 * @author Pieter De baets
 */
public class ChatApplication {
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
    }

    private void start(String[] args) {
        // perform auto login?
        if(args!= null && args.length == 2) {
            Configuration configuration = new Configuration(args[0]);
            Account account = configuration.getAccount();
            if(account != null && account.authenticate(args[1])) {
                this.configuration = configuration;
                performTestLogin();
            } else {
                System.err.println("Failed to login with provided credentials");
            }
        }

        if(configuration == null) {
            // show login dialog
            LoginController loginController = new LoginController();
            configuration = loginController.run(appWindow);
            if(configuration == null) {
                // user closed window
                System.exit(0);
            }
        }

        appWindow.initApplicationView();
        appWindow.setStatus(I18n.get("ch9k.core", "booting"));
    }

    public void logout() {
        new Thread(new Runnable() {
            public void run() {
                configuration = null;
                start(null);
            }
        }).start();
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
     * Get the conversation manager
     * @return conversationManager
     */
    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    /**
     * Perform a fake login
     * (to be used for testing purposes only!)
     */
    public void performTestLogin() {
        if(configuration == null || configuration.getAccount() == null) {
            configuration = new Configuration("CH9K");
            configuration.setAccount(new Account("CH9K", "password"));
        }

        ContactList contacts = configuration.getAccount().getContactList();

        try {
            contacts.addContact(new Contact("jaspervdj",
                    InetAddress.getByName("jaspervdj.be"), false));
            contacts.addContact(new Contact("Javache",
                    InetAddress.getByName("javache.be"), false));
            contacts.addContact(new Contact("nudded",
                    InetAddress.getByName("ugent.be"), false));
            contacts.addContact(new Contact("jpanneel",
                    InetAddress.getByName("google.be"), false));
            contacts.addContact(new Contact("robust2",
                    InetAddress.getByName("google.com"), false));
        } catch (UnknownHostException ex) {}
    }
}
