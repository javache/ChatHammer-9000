package ch9k.chat;


import ch9k.chat.events.CloseConversationEvent;
import ch9k.chat.events.ConversationEvent;
import ch9k.chat.events.NewConversationEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jens Panneel
 */
public class ConversationManager implements EventListener{
    private Map<Contact, Conversation> conversations;

    public ConversationManager() {
        conversations = new HashMap<Contact, Conversation>();
        EventPool.getAppPool().addListener(this, new TypeEventFilter(ConversationEvent.class));
    }

    /**
     * Start a conversation with the given contact
     * @param contact The contact to start a new conversation with.
     * @return conversation The started conversation
     */
    public Conversation startConversation(Contact contact, boolean initiatedByMe) {
        // TODO pass true the correct boolean, get info from event
        Conversation conversation = new Conversation(contact, initiatedByMe);
        // TODO what if there is already a conversation with this contact?
        conversations.put(contact, conversation);
        return conversation;
    }

    /**
     * Close the conversation with the given contact
     * @param contact The contact you want to stop chatting with
     */
    public void closeConversation(Contact contact) {
        conversations.remove(contact);
    }

    /**
     * Get the conversation you have with the given contact
     * @param contact
     * @return conversation
     */
    public Conversation getConversation(Contact contact) {
        return conversations.get(contact);
    }

    @Override
    public void handleEvent(Event event) {
        if(event instanceof NewConversationEvent) {
            NewConversationEvent newConversationEvent = (NewConversationEvent)event;
            this.startConversation(newConversationEvent.getContact(), !newConversationEvent.isExternal());
        }

        if(event instanceof CloseConversationEvent) {
            CloseConversationEvent closeConversationEvent = (CloseConversationEvent)event;
            this.closeConversation(closeConversationEvent.getContact());
        }
    }
    
}
