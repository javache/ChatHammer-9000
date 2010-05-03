package ch9k.plugins;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.NewChatMessageEvent;
import ch9k.chat.event.NewConversationSubjectEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Abstract TextAnalyzer class.
 * @author Jasper Van der Jeugt
 */
public abstract class TextAnalyzer extends AbstractPlugin
        implements EventListener {
    /**
     * I accidentaly your logger
     */
    private static final Logger logger = Logger.getLogger(TextAnalyzer.class);

    /**
     * Filter to take out punctuation marks.
     */
    private static Pattern punctuation = Pattern.compile("[.,!?&\"]");

    /**
     * Set of words to be ignored.
     */
    private static Set<String> noiseWords;

    /* Statically parse the list of words to be ignored. */
    static {
        noiseWords = new HashSet<String>();
        String fileName = "/ch9k/plugins/NoiseWords.txt";

        try {
            /* Open up the reader. */
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    TextAnalyzer.class.getResourceAsStream(fileName)));

            /* Parse every line and add it to the list. */
            String line = reader.readLine();
            while(line != null) {
                String word = normalizeWord(line);
                /* Skip empty lines and comments. */
                if(word.length() > 0 && word.charAt(0) != '#') {
                    noiseWords.add(word);
                }
                line = reader.readLine();
            }

            /* Do not forget to close our stuff. */
            reader.close();
        /* Something *bad* happened, warn the user. */
        } catch (IOException exception) {
            logger.warn("Could not read NoiseWords list: " + exception);
        }
    }

    @Override
    public void enablePlugin(Conversation conversation) {
        super.enablePlugin(conversation);
        EventFilter filter = new ConversationEventFilter(
                NewChatMessageEvent.class, conversation);
        EventPool.getAppPool().addListener(this, filter);
    }

    @Override
    public void disablePlugin() {
        super.disablePlugin();
        EventPool.getAppPool().removeListener(this);
    }

    @Override
    public void handleEvent(Event e) {
        NewChatMessageEvent event = (NewChatMessageEvent) e;

        /* Get the raw text from the messages. */
        ChatMessage[] chatMessages =
                getConversation().getMessages(getMaxNumberOfMessages());
        String[] messages = new String[chatMessages.length];
        for(int i = 0; i < messages.length; i++) {
            messages[i] = chatMessages[i].getRawText();
        }

        /* Create a new subject. */
        String[] result = getSubjects(messages);
        ConversationSubject subject = new ConversationSubject(result);

        /* Throw the new event. */ 
        NewConversationSubjectEvent subjectEvent =
                new NewConversationSubjectEvent(getConversation(), subject);
        EventPool.getAppPool().raiseNetworkEvent(subjectEvent);
    }

    /**
     * Get the max number of messages to take from the conversation.
     * @return The max number of messages to take.
     */
    public abstract int getMaxNumberOfMessages();

    /**
     * Get the conversation subject as strings.
     * @param messages The different messages in the conversation.
     * @return Strings representing conversation subjects.
     */
    public abstract String[] getSubjects(String[] messages);

    /**
     * Build a frequency map of words in the conversation.
     * @param messages Messages to analyze.
     * @return A frequency map from words to their frequencies.
     */
    public Map<String, Integer> getFrequencyMap(String[] messages) {
        Map<String, Integer> frequencyMap = new TreeMap<String, Integer>();

        /* Loop through all messages. */
        for(String message: messages) {
            /* Remove all punctuation and split on space characters. */
            Matcher matcher = punctuation.matcher(message);
            String[] words = matcher.replaceAll("").split("\\s+");

            /* Now loop through all words in the message. */
            for(String word: words) {
                /* Trim and lowercase the string, then store it's frequency. */
                String key = normalizeWord(word);
                if(!isNoiseWord(key)) {
                    Integer frequency = frequencyMap.get(key);
                    if(frequency == null) {
                        frequencyMap.put(key, 1);
                    } else {
                        frequencyMap.put(key, frequency.intValue() + 1);
                    }
                }
            }
        }

        return frequencyMap;
    }

    /**
     * Normalize a word. This includes trimming and putting the word in
     * lowercase.
     * @param word Word to normalize.
     * @return Normalized version of the word.
     */
    public static String normalizeWord(String word) {
        return word.trim().toLowerCase();
    }

    /**
     * Check if a given word is a predefined noise word.
     * @param word String to check.
     * @return If the given word is noise.
     */
    public boolean isNoiseWord(String word) {
        /* We consider all < 1 character words as noise. */
        String normalized = normalizeWord(word);
        return normalized.length() <= 1 || noiseWords.contains(normalized);
    }
}
