package be.ugent.zeus;

import ch9k.chat.Conversation;
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
    public void enablePlugin(Conversation conversation) {
    }

    @Override
    public void disablePlugin(Conversation conversation) {
    }

    @Override
    public String getPrettyName() {
        return "DummyPlugin";
    }
}
