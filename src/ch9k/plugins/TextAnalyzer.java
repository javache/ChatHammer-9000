package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.chat.events.NewConversationSubjectEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;

/**
 * Abstract TextAnalyzer class.
 * @author Jasper Van der Jeugt
 */
public abstract class TextAnalyzer extends AbstractPlugin
        implements EventListener {
    @Override
    public void enable(Conversation conversation) {
        super.enable(conversation);
        EventFilter filter = new TypeEventFilter(NewChatMessageEvent.class);
        EventPool.getAppPool().addListener(this, filter);
    }

    @Override
    public void disable() {
        EventPool.getAppPool().removeListener(this);
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is not relevant. */
        NewChatMessageEvent event = (NewChatMessageEvent) e;
        if(!isRelevant(event)) return;

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
}
