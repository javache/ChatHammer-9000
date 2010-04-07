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
