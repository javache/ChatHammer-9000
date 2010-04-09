package ch9k.core;

/**
 * The main application, OMG!
 *
 * @author Bruno
 */
public class ChatApplication {

    public static ChatApplication getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* Helper-class for singleton, aka Bill Pugh's method */
    private static class SingletonHolder {
         private static final ChatApplication INSTANCE = new ChatApplication();
    }


    private Account account;

    public Account getAccount() {
        return account;
    }

    private ChatApplication() {
        account = new Account("Tetten", "tetten");
    }
    
}
