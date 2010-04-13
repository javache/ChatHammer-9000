package ch9k.plugins;

import ch9k.eventpool.EventListener;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.chat.events.NewChatMessageEvent;

/**
 * Abstract TextAnalyzer class.
 * @author Jasper Van der Jeugt
 */
public abstract class TextAnalyzer extends AbstractPlugin
        implements EventListener {
    /**
     * Constructor.
     */
    public TextAnalyzer() {
        // TODO: Register as listener
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is no NewChatMessageEvent. */
        if(!(e instanceof NewChatMessageEvent)) return;
        NewChatMessageEvent event = (NewChatMessageEvent) e;

        /* Return if the event is not relevant. */
        if(!isRelevant(event)) return;

        /* For now, we just send new images on new text. */
        String[] result = getSubject();
    }

    /**
     * Get the conversation subject as strings.
     * @return Strings representing conversation subjects.
     */
    public abstract String[] getSubject();
}
