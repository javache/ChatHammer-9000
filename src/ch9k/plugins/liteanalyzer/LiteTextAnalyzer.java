package ch9k.plugins.liteanalyzer;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.Plugin;
import ch9k.plugins.TextAnalyzer;
import ch9k.plugins.TextAnalyzerPreferencePane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A lite text analyzer.
 */
public class LiteTextAnalyzer extends TextAnalyzer {
    /**
     * Constructor.
     * @param plugin Corresponding plugin.
     * @param conversation Conversation to analyze.
     * @param settings Local settings to use.
     */
    public LiteTextAnalyzer(Plugin plugin,
            Conversation conversation, Settings settings) {
        super(plugin, conversation, settings);
    }

    @Override
    public List<String> getSubjects(String[] messages) {
        /* Create a frequency map. */
        Map<String, Integer> frequencies = getFrequencyMap(messages);

        /* Add keys to a list and sort it. */
        List<Subject> list = new ArrayList<Subject>();
        for(String word: frequencies.keySet()) {
            list.add(new Subject(word, frequencies.get(word)));
        }
        Collections.sort(list);

        /* Create a list with the result. */
        List<String> result = new ArrayList<String>();
        for(Subject subject: list) {
            result.add(subject.getSubject());
        }

        return result;
    }

    /**
     * Private class to represent a single subject.
     */
    private class Subject implements Comparable<Subject> {
        /**
         * Word denoting the subject.
         */
        private String subject;

        /**
         * Frequency of the subject.
         */
        private int frequency;

        /**
         * Constructor.
         * @param subject The single subject.
         * @param frequency Frequency of this subject.
         */
        public Subject(String subject, int frequency) {
            this.subject = subject;
            this.frequency = frequency;
        }

        /**
         * Obtain the actual subject.
         * @return The simple subject.
         */
        public String getSubject() {
            return subject;
        }

        @Override
        public int compareTo(Subject subject) {
            return subject.frequency - frequency;
        }
    }
}
