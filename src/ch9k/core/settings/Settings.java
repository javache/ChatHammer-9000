package ch9k.core.settings;

import java.awt.EventQueue;
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;

/**
 * Abstract settings class, storing key-value pairs.
 */
public class Settings implements Serializable {
    /**
     * False string value.
     */
    public final static String FALSE = "false";

    /**
     * True string value.
     */
    public final static String TRUE = "true";

    /**
     * Map delegate.
     */
    private Map<String, String> settings;

    /**
     * Registered listeners.
     */
    private transient EventListenerList listenerList;

    /**
     * Constructor.
     */
    public Settings() {
        settings = new HashMap<String, String>();
        listenerList = new EventListenerList();
    }

    /**
     * Get a setting.
     * @param key Key of the setting to get.
     * @return Value of the requested setting.
     */
    public String get(String key) {
        return settings.get(key);
    }

    /**
     * Change a setting.
     * @param key Key of the setting to change.
     * @param value New setting value.
     */
    public void set(String key, String value) {
        String old = settings.get(key);
        if(old == null && value == null) {
            return;
        } else if(old != null && !old.equals(value)) {
            settings.put(key, value);
            fireSettingsChanged(key, value);
        }
    }

    /**
     * Register a listener.
     * @param listener SettingsChangeListener to add.
     */
    public void addSettingsListener(SettingsChangeListener listener) {
        listenerList.add(SettingsChangeListener.class, listener);
    }

    /**
     * Remove a listener.
     * @param listener Listener to remove.
     */
    public void removeSettinsListener(SettingsChangeListener listener) {
        listenerList.remove(SettingsChangeListener.class, listener);
    }

    /**
     * Throw a new SettingsChangeEvent.
     * @param key Key of which the value was changed.
     * @param value The new value.
     */
    public void fireSettingsChanged(String key, String value) {
        final SettingsChangeEvent event =
                new SettingsChangeEvent(this, key, value);
        if(EventQueue.isDispatchThread()) {
            settingsChange(event);
        } else {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    settingsChange(event);
                }
            });
        }
    }

    /**
     * Send the settings change event to the listeners.
     * @param event The event to send.
     */
    private void settingsChange(SettingsChangeEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if(listeners[i] == SettingsChangeListener.class) {
                SettingsChangeListener listener = 
                        (SettingsChangeListener) listeners[i + 1];
                listener.settingsChanged(event);
            }
        }
    }
}
