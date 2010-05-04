package ch9k.core.settings.event;

import ch9k.eventpool.Event;
import javax.swing.JPanel;

/**
 *
 * @author toon
 */
public class PreferencePaneEvent extends Event {

    private String title;

    private JPanel panel;

    public PreferencePaneEvent(String title, JPanel panel) {
        this.title = title;
        this.panel = panel;
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


}
