package ch9k.core;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.HashMap;
import javax.swing.UIManager;

/**
 * A class responsible for the internationalization of string literals.
 */
public class I18n {
    /**
     * The resource bundles.
     */
    private static final Map<String, ResourceBundle> bundles =
            new HashMap<String, ResourceBundle>();

    /* Obtain the top level locale, and load the resource bundle. */
    static {
        UIManager.getDefaults().addResourceBundle("ch9k.MessageBundle");
    }

    /**
     * Get a localized message.
     * @param namespace The message namespace.
     * @param key The message key.
     * @return The message in the correct Locale.
     */
    public static String get(String namespace, String key) {
        ResourceBundle bundle = bundles.get(namespace);
        if(bundle == null) {
            bundle = ResourceBundle.getBundle(namespace + ".MessageBundle");
            bundles.put(namespace, bundle);
        }

        try {
            return bundle.getString(key);
        } catch(MissingResourceException exception) {
            return null;
        }
    }

    /**
     * Get a formatted message.
     * @param namespace The message namespace.
     * @param key The message key.
     * @param arguments The message arguments to be given to the key.
     * @return The message in the correct Locale.
     */
    public static String get(String namespace, String key,
            Object ... arguments) {
        return MessageFormat.format(get(namespace, key), arguments);
    }

    /** 
     * Get the mnemonic key of a message.
     * 
     * This will first try to take the mnemonic from the key (key + "_mn"). If
     * such a key is not found, it will take the first character from the
     * message returned by the key.
     *
     * Therefore, if the mnemonic is the first character of the message, it is
     * not nessecary to specify a mnemonic in the properties file.
     *
     * @param namespace The bundle namespace.
     * @param key The mnemonic key.
     * @return A localized mnemonic for that key.
     */
    public static int getMnemonic(String namespace, String key) {
        /* First try the _mn key */
        String string = get(namespace, key + "_mn");
        /* Not found, try the regular key */
        if(string == null) {
            string = get(namespace, key);
        }

        /* Calculate the mnemonic based on the character. */
        return string.toLowerCase().charAt(0) - 'a' + KeyEvent.VK_A;
    }
}
