package ch9k.core;

import ch9k.configuration.Configuration;

/**
 * The login model (not the gui)
 * handles the loading of the account (and verifying it)
 */
public class Login {
    
    /**
     * this method has to be called when a user wants to login
     */
    public Configuration loadAccount(String username, String password) {
        Configuration config = new Configuration(username);
        config.loadExisting();
        return config.getAccount().authenticate(password)? config : null;
    }
    
    /**
     * creates a new account
     */
    public Configuration newAccount(String username,String password) {
        Configuration config = new Configuration(username);
        config.createNew(username, password);
        return config;
        
    }
    
} 
