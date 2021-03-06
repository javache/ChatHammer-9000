package ch9k.plugins;

import ch9k.core.I18n;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;
import ch9k.core.settings.SettingsChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Preference pane for an image provider plugin.
 */
public class ImageProviderPreferencePane
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
    public ImageProviderPreferencePane(final Settings settings) {
        this.settings = settings;
        add(new JLabel(I18n.get("ch9k.plugins", "enable_safe_search")));
        
        safeSearchCheckBox = new JCheckBox();
        add(safeSearchCheckBox);

        safeSearchCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                settings.setBoolean(SAFE_SEARCH,
                        !settings.getBoolean(SAFE_SEARCH));
            }
        });

        safeSearchCheckBox.setSelected(settings.getBoolean(SAFE_SEARCH));
        settings.addSettingsListener(this);
    }

    @Override
    public void settingsChanged(SettingsChangeEvent event) {
        if(SAFE_SEARCH.equals(event.getKey())) {
            safeSearchCheckBox.setSelected(settings.getBoolean(SAFE_SEARCH));
        }
    }
}
