package ch9k.core.settings;

import java.io.Serializable;

/**
 * Event that represents a settings change.
 */
public class SettingsChangeEvent implements Serializable {
    /**
     * Key that was changed.
     */
    private String key;

    /**
     * Value that was changed.
     */
    private String value;

    /**
     * Constructor.
     * @param key Key that was changed.
     * @param value Value that was changed.
     */
    public SettingsChangeEvent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the changed key.
     * @return The changed key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the changed value.
     * @return The changed value.
     */
    public String getValue() {
        return value;
    }
}
