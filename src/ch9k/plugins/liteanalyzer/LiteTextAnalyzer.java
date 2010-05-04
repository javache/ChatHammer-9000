package ch9k.plugins.liteanalyzer;

import ch9k.chat.Conversation;
import ch9k.plugins.TextAnalyzer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A lite text analyzer.
 */
public class LiteTextAnalyzer extends TextAnalyzer {
    /**
     * Number of subjects to return.
     */
    private static final int NUM_SUBJECTS = 3;

    /**
     * Constructor.
     * @param conversation Conversation to analyze.
     */
    public LiteTextAnalyzer(Conversation conversation) {
        super(conversation);
    }

    @Override
    public int getMaxNumberOfMessages() {
        return 10;
    }

    @Override
    public String[] getSubjects(String[] messages) {
        /* Create a frequency map. */
        Map<String, Integer> frequencies = getFrequencyMap(messages);

        /* Add keys to a list and sort it. */
        List<Subject> list = new ArrayList<Subject>();
        for(String word: frequencies.keySet()) {
            list.add(new Subject(word, frequencies.get(word)));
        }
        Collections.sort(list);

        /* Take the NUM_SUBJECTS most important subjects. */
        int numSubjects = list.size() > NUM_SUBJECTS ?
                NUM_SUBJECTS : list.size();
        String[] subject = new String[numSubjects];
        for(int i = 0; i < numSubjects; i++) {
            subject[i] = list.get(i).getSubject();
        }

        return subject;
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
