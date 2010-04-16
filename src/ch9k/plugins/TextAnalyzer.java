package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.chat.events.NewConversationSubjectEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract TextAnalyzer class.
 * @author Jasper Van der Jeugt
 */
public abstract class TextAnalyzer extends AbstractPlugin
        implements EventListener {
    /**
     * Filter to take out punctuation marks.
     */
    private static Pattern punctuation = Pattern.compile("[.,!?&\"]");

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

        /* Create a new subject. */
        String[] messages =
                getConversation().getMessages(getMaxNumberOfMessages());
        String[] result = getSubjects(messages);
        ConversationSubject subject =
                new ConversationSubject(getConversation(), result);

        /* Throw the new event. */ 
        Event subjectEvent =
                new NewConversationSubjectEvent(getConversation(), subject);
        EventPool.getAppPool().raiseEvent(subjectEvent);
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
                String key = word.trim().toLowerCase();
                Integer frequency = frequencyMap.get(key);
                if(frequency == null) {
                    frequencyMap.put(key, 1);
                } else {
                    frequencyMap.put(key, frequency.intValue() + 1);
                }
            }
        }

        return frequencyMap;
    }
}
