package be.jaspervdj.wordcloud;

import ch9k.core.I18n;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;
import ch9k.core.settings.SettingsChangeListener;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Preference pane for the word cloud plugin.
 */
public class WordCloudPreferencePane
        extends JPanel implements SettingsChangeListener, ChangeListener {
    /**
     * Maximum number of words to show.
     */
    public static final String MAX_WORDS = "maxWords";

    /**
     * Settings to manipulate.
     */
    private final Settings settings;

    /**
     * MAX_WORDS spinner.
     */
    private final JSpinner maxWordsSpinner;

    /**
     * Constructor.
     * @param settings Settings to manipulate.
     */
    public WordCloudPreferencePane(final Settings settings) {
        this.settings = settings;

        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel maxWordsLabel = new JLabel(
                I18n.get("be.jaspervdj.wordcloud", "max_words"));
        maxWordsSpinner = new JSpinner(new SpinnerNumberModel(
                settings.getInt(MAX_WORDS),
                5, 100, 5));

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(maxWordsLabel))
            .addGroup(layout.createParallelGroup()
                .addComponent(maxWordsSpinner)));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(maxWordsLabel)
                .addComponent(maxWordsSpinner, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        setLayout(layout);

        maxWordsSpinner.addChangeListener(this);
        settings.addSettingsListener(this);
    }

    @Override
    public void settingsChanged(SettingsChangeEvent event) {
        if(MAX_WORDS.equals(event.getKey())) {
            maxWordsSpinner.setValue(settings.getInt(MAX_WORDS));
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if(event.getSource() == maxWordsSpinner) {
            settings.setInt(MAX_WORDS,
                    (Integer) maxWordsSpinner.getValue());
        }
    }
}
