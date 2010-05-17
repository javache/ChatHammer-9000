package ch9k.configuration;

import ch9k.core.Account;
import ch9k.core.settings.Settings;
import ch9k.plugins.PluginManager;

/**
 * Stores local settings & properties of the application.
 * @author Bruno
 */
public class Configuration {
    /**
     * Account-instance for current user
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

    /**
     * Username
     */
    private String username;

    /**
     * Settings instance used by the app
     */
    private Settings settings;

    /**
     * Create new storage object for this user
     * @param username
     */
    public Configuration(String username) {
        storage = new Storage(username);
        this.username = username;
        
        PersistentDataObject pdo = storage.fetch("settings");
        if(pdo != null) {
            settings = new Settings(pdo);
            storage.store("settings", settings);

            String proxy = settings.get("proxy");
            String proxyPort = settings.get("proxyPort");
            
            if(proxy != null && !proxy.isEmpty()){
                System.setProperty("http.proxyHost", proxy);
                if(proxyPort == null || proxyPort.isEmpty()){
                    proxyPort = "80";
                }
                System.setProperty("http.proxyPort", proxyPort);
            }
        }
    }

    /**
     * Getter for account
     * @return account object for current user.
     */
    public Account getAccount() {
        if(account == null) {
            //Load the account object, or create a new one
            PersistentDataObject pdo = storage.fetch("account");
            if(pdo != null) {
                account = new Account(pdo);
                storage.store("account", account);
            }
        }

        return account;
    }

    public Settings getSettings() {
        if(settings == null) {
            //Load the settings object, or create a new one
            PersistentDataObject pdo = storage.fetch("settings");
            if(pdo != null) {
                settings = new Settings(pdo);
                storage.store("settings", settings);
            } else {
                settings = new Settings();
                storage.store("settings", settings);
            }
        }

        return settings;
    }

    /**
     * Setter for account
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
        storage.store("account", account);
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        storage.store("settings", settings);
    }

    /**
     * Getter for pluginmanager
     * @return PluginManager currently in use.
     */
    public PluginManager getPluginManager() {
        if(pluginManager == null) {
            //Load the pluginManager object, or create a new one
            PersistentDataObject pdo = storage.fetch("pluginManager");
            if(pdo != null) {
                pluginManager = new PluginManager(pdo);
                storage.store("pluginManager", pluginManager);
            } else {
                pluginManager = new PluginManager();
                storage.store("pluginManager", pluginManager);
            }
        }
        return pluginManager;
    }

    /**
     * Save the configuration data
     */
    public void save() {
        storage.save();
    }
}
