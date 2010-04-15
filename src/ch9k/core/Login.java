package ch9k.core;

import ch9k.configuration.Configuration;
import ch9k.core.gui.LoginView;
import java.awt.EventQueue;

/**
 * The login model (not the gui)
 * handles the loading of the account (and verifying it)
 * @author Bruno Corijn
 * @author Pieter De Baets
 */
public class Login {
    Configuration configuration;

    /**
     * Show the login-window, will block until a valid configuration is acquired
     * @return configuration
     */
    public synchronized Configuration run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginView view = new LoginView(Login.this);
            }
        });

        while(configuration == null) {
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        return configuration;
    }
    
    /**
     * Try loading a configuration by authenticating an existing user
     * @param username
     * @param password
     * @return succes
     */
    public synchronized boolean login(String username, String password) {
        Configuration configuration = new Configuration(username);
        Account account = configuration.getAccount();

        if(account != null && account.authenticate(password)) {
            this.configuration = configuration;
            notifyAll();
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Create a configuration for a new user
     * @param username
     * @param password
     */
    public synchronized void register(String username, String password) {
        configuration = new Configuration(username);
        configuration.setAccount(new Account(username, password));

        notifyAll();
    }
} 
