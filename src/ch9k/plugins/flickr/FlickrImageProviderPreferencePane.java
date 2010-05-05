package ch9k.plugins.flickr;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;
import ch9k.core.settings.SettingsChangeListener;

/**
 * Preference pane for the Flickr plugin.
 */
public class FlickrImageProviderPreferencePane
        extends JPanel implements SettingsChangeListener {
    /**
     * Safe search key.
     */
    public static final String SAFE_SEARCH = "safeSearch";

    /**
     * Settings to manipulate.
     */
    private final Settings settings;

    /**
     * Safe search checkbox.
     */
    private final JCheckBox safeSearchCheckBox;

    /**
     * Constructor.
     * @param settings Settings to manipulate.
     */
    public FlickrImageProviderPreferencePane(final Settings settings) {
        this.settings = settings;
        add(new JLabel("Enable safe search:"));
        
        safeSearchCheckBox = new JCheckBox();
        add(safeSearchCheckBox);

        safeSearchCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                settings.setBoolean(SAFE_SEARCH,
                        !settings.getBoolean(SAFE_SEARCH));
            }
        });

        safeSearchCheckBox.setSelected(settings.getBoolean(SAFE_SEARCH));
    }

    @Override
    public void settingsChanged(SettingsChangeEvent event) {
        if(SAFE_SEARCH.equals(event.getKey())) {
            safeSearchCheckBox.setSelected(settings.getBoolean(SAFE_SEARCH));
        }
    }
}
