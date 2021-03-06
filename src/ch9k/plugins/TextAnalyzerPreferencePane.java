package ch9k.plugins;

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
 * Preference pane for a LiteTextAnalyzer.
 */
public class TextAnalyzerPreferencePane
        extends JPanel implements SettingsChangeListener, ChangeListener {
    /**
     * Maximum number of subjects to search.
     */
    public static final String MAX_SUBJECTS = "maxSubjects";

    /**
     * Maximum number of messages to analyze.
     */
    public static final String MAX_MESSAGES = "maxMessages";

    /**
     * Trigger interval, in seconds.
     */
    public static final String TRIGGER_INTERVAL = "triggerInterval";

    /**
     * Settings to manipulate.
     */
    private final Settings settings;

    /**
     * NUM_SUBJECTS spinner.
     */
    private final JSpinner maxSubjectsSpinner;

    /**
     * MAX_MESSAGES spinner.
     */
    private final JSpinner maxMessagesSpinner;

    /**
     * TRIGGER_INTERVAL spinner.
     */
    private final JSpinner triggerIntervalSpinner;

    /**
     * Constructor.
     * @param settings Settings to manipulate.
     */
    public TextAnalyzerPreferencePane(final Settings settings) {
        this.settings = settings;

        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel maxSubjectsLabel = new JLabel(
                I18n.get("ch9k.plugins", "max_subjects"));
        JLabel maxMessagesLabel = new JLabel(
                I18n.get("ch9k.plugins", "max_messages"));
        JLabel triggerIntervalLabel = new JLabel(
                I18n.get("ch9k.plugins", "trigger_interval"));
        maxSubjectsSpinner = new JSpinner(new SpinnerNumberModel(
                settings.getInt(MAX_SUBJECTS), 1, 5, 1));
        maxMessagesSpinner = new JSpinner(new SpinnerNumberModel(
                settings.getInt(MAX_MESSAGES), 1, 30, 1));
        triggerIntervalSpinner = new JSpinner(new SpinnerNumberModel(
                settings.getInt(TRIGGER_INTERVAL), 5, 80, 5));

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(maxSubjectsLabel)
                .addComponent(maxMessagesLabel)
                .addComponent(triggerIntervalLabel))
            .addGroup(layout.createParallelGroup()
                .addComponent(maxSubjectsSpinner)
                .addComponent(maxMessagesSpinner)
                .addComponent(triggerIntervalSpinner)));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(maxSubjectsLabel)
                .addComponent(maxSubjectsSpinner, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(maxMessagesLabel)
                .addComponent(maxMessagesSpinner, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(triggerIntervalLabel)
                .addComponent(triggerIntervalSpinner,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

        setLayout(layout);

        maxSubjectsSpinner.addChangeListener(this);
        maxMessagesSpinner.addChangeListener(this);
        triggerIntervalSpinner.addChangeListener(this);
        settings.addSettingsListener(this);
    }

    @Override
    public void settingsChanged(SettingsChangeEvent event) {
        if(MAX_SUBJECTS.equals(event.getKey())) {
            maxSubjectsSpinner.setValue(settings.getInt(MAX_SUBJECTS));
        }

        if(MAX_MESSAGES.equals(event.getKey())) {
            maxMessagesSpinner.setValue(settings.getInt(MAX_MESSAGES));
        }

        if(TRIGGER_INTERVAL.equals(event.getKey())) {
            triggerIntervalSpinner.setValue(settings.getInt(TRIGGER_INTERVAL));
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if(event.getSource() == maxSubjectsSpinner) {
            settings.setInt(MAX_SUBJECTS,
                    (Integer) maxSubjectsSpinner.getValue());
        }

        if(event.getSource() == maxMessagesSpinner) {
            settings.setInt(MAX_MESSAGES,
                    (Integer) maxMessagesSpinner.getValue());
        }

        if(event.getSource() == triggerIntervalSpinner) {
            settings.setInt(TRIGGER_INTERVAL,
                    (Integer) triggerIntervalSpinner.getValue());
        }
    }
}
