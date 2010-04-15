package ch9k.core;

import ch9k.chat.ConversationManager;
import ch9k.configuration.Configuration;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

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
    
    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(ChatApplication.class);

    private Configuration configuration;
    private ConversationManager conversationManager;

    public static void main(String[] args) {
        ChatApplication app = ChatApplication.getInstance();
        app.start();
    }

    private ChatApplication() {
        conversationManager = new ConversationManager();
    }

    private void start() {
        // use a native look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
            logger.info("Unable to load native look & feel");
        }

        // set the menu bar on top of the screen on mac
		System.setProperty ("apple.laf.useScreenMenuBar", "true");

        // show login dialog
        Login loginController = new Login();
        configuration = loginController.run();
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
        configuration = new Configuration("CH9K");
        configuration.setAccount(new Account("CH9K", "password"));
    }
}
