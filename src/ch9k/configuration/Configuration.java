package ch9k.configuration;

import ch9k.core.Account;
import ch9k.plugins.PluginManager;

/**
 * Stores local settings & properties of the application.
 *
 * @author Bruno
 */
public class Configuration {
    /**
     * account object for current user
     */
    private Account account;

    /**
     * PluginManager currently in use
     */
    private PluginManager pluginManager;

    /**
     * Storage, used to store stuff
     */
    private Storage storage;

    private String username;

    public Configuration(String username) {
        //Create new storage object for this user
        storage = new Storage(username);
        this.username = username;
    }

    /**
     * Getter for account
     * @return account object for current user.
     */
    public Account getAccount() {
        if (account == null) {
            //Load the account object, or create a new one
            PersistentDataObject pdo = storage.fetch("account");
            if (pdo != null) {
                account = new Account(pdo);
            } else {
                account = new Account(username);
            }
            storage.store("account", account);
        }
        return account;
    }

    /**
     * Getter for pluginmanager
     * @return PluginManager currently in use.
     */
    public PluginManager getPluginManager() {
        //Create a new pluginManager, should initiate himself
        if (pluginManager == null) {
            pluginManager = new PluginManager();
        }
        return pluginManager;
    }
}
