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
     */
    public synchronized void loadConfiguration(String username, String password) {
        Configuration configuration = new Configuration(username);

        if(configuration.getAccount().authenticate(password)) {
            this.configuration = configuration;
            notifyAll();
        }
    }
    
    /**
     * Create a configuration for a new user
     * @param username
     */
    public synchronized void createConfiguration(String username) {
        configuration = new Configuration(username);
        notifyAll();
    }
} 
