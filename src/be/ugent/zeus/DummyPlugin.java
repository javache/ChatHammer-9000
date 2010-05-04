package be.ugent.zeus;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.Plugin;

/**
 * A dummy plugin that does nothing at all.
 * @author Jasper Van der Jeugt
 */
public class DummyPlugin implements Plugin {
    @Override
    public boolean isEnabled(Conversation conversation) {
        return false;
    }

    @Override
    public void enablePlugin(Conversation conversation, Settings settings) {
    }

    @Override
    public void disablePlugin(Conversation conversation) {
    }

    @Override
    public String getPrettyName() {
        return "DummyPlugin";
    }

    @Override
    public Settings getSettings() {
        return null;
    }
}
