package ch9k.core.settings;

import java.util.EventListener;

/**
 * Listener that gets informed of setting changes.
 */
public interface SettingsChangeListener extends EventListener {
    void settingsChanged(SettingsChangeEvent event);
}
