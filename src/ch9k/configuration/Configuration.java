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

    public Configuration(String username) {
        //Create new storage object for this user
        storage = new Storage(username);

        //Create a new pluginManager, should initiate himself
        pluginManager = new PluginManager();
    }

    public void loadExisting(){
        //Load the account object, this might fail tho'
        PersistentDataObject pdo = storage.fetch("account");
        if(pdo != null){
            account = new Account(pdo);
        } else {
            account = null;
        }
        storage.store("account", account);
    }

    public void createNew(String username, String password){
        account = new Account(username, password);
        storage.store("account", account);
    }

    /**
     * Getter for account
     * @return account object for current user.
     */
    public Account getAccount() {
            return account;
    }

    /**
     * Getter for pluginmanager
     * @return PluginManager currently in use.
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
