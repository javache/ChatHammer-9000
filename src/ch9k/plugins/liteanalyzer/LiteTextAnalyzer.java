package ch9k.plugins.liteanalyzer;

import java.util.Collections;
import ch9k.plugins.TextAnalyzer;
import ch9k.chat.Conversation;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A lite text analyzer. The PRO version is available for onle 29.99 euros!
 */
public class LiteTextAnalyzer extends TextAnalyzer {
    /**
     * Number of last messages to use.
     */
    private static final int MAX_MESSSAGES = 10;

    /**
     * Number of subjects to return.
     */
    private static final int NUM_SUBJECTS = 3;

    /**
     * Filter to take out punctuation marks.
     */
    private static Pattern punctuation = Pattern.compile("[.,!?&\"]");

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

    @Override
    public String[] getSubject() {
        /* Obtain the MAX_MESSSAGES last messages from the conversation. */
        String[] messages = getConversation().getMessages(MAX_MESSSAGES);
        
        /* Create a frequency map. */
        Map<String, Integer> frequencies = new TreeMap<String, Integer>();

        /* Build a frequency map. */
        for(String message: messages) {
            Matcher matcher = punctuation.matcher(message);
            String[] words = matcher.replaceAll("").split("\\s+");
            for(String word: words) {
                String key = word.trim();
                Integer frequency = frequencies.get(key);
                if(frequency == null) frequencies.put(key, 1);
                else frequencies.put(key, frequency.intValue() + 1);
            }
        }

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
}
