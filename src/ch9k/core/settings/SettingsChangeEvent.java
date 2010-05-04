package ch9k.core.settings;

import java.io.Serializable;

/**
 * Event that represents a settings change.
 */
public class SettingsChangeEvent implements Serializable {
    /**
     * Source of the event.
     */
    private Settings source;

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
     * @param source Source of the event.
     * @param key Key that was changed.
     * @param value Value that was changed.
     */
    public SettingsChangeEvent(Settings source, String key, String value) {
        this.source = source;
        this.key = key;
        this.value = value;
    }

    /**
     * Get the event source.
     * @return The event source.
     */
    public Settings getSource() {
        return source;
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
