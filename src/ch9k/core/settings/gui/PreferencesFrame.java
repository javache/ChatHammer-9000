package ch9k.core.settings.gui;

import ch9k.core.settings.event.PreferencePaneEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author toon
 */
public class PreferencesFrame extends JFrame implements EventListener {

    private JTabbedPane tabbedPane;

    public PreferencesFrame() {
        super("Preferences");

        EventPool.getAppPool().addListener(this, new EventFilter(PreferencePaneEvent.class));

        tabbedPane = new JTabbedPane();
        setContentPane(tabbedPane);
        pack();
    }

    @Override
    public void handleEvent(Event ev) {
        PreferencePaneEvent event = (PreferencePaneEvent)ev;
        tabbedPane.add(event.getTitle(), event.getPanel());
        pack();
        repaint();
    }

    public void close() {
        
    }

}
