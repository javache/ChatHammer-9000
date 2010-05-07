package ch9k.core.settings.event;

import ch9k.eventpool.Event;
import javax.swing.JPanel;

/**
 *
 * @author toon "fuck comments" willems
 */
public class PreferencePaneEvent extends Event {
    private String title;

    private JPanel panel;

    boolean shouldAdd;

    public PreferencePaneEvent(String title, JPanel panel) {
        this(title, panel, true);
    }

    public PreferencePaneEvent(String title, JPanel panel, boolean paneActive) {
        this.title = title;
        this.panel = panel;
        this.shouldAdd = paneActive;
    }

    @Override
    public Object getSource() {
        return panel;
    }

    public String getTitle() {
        return title;
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean shouldAdd() {
        return shouldAdd;
    }
}
