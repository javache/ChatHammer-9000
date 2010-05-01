package ch9k.chat;

import ch9k.chat.event.CloseConversationEvent;
import ch9k.chat.event.ConversationEvent;
import ch9k.chat.event.NewConversationEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the active conversations
 * @author Jens Panneel
 */
public class ConversationManager implements EventListener{
    private Map<Contact, Conversation> conversations;

    public ConversationManager() {
        conversations = new HashMap<Contact, Conversation>();
        EventPool.getAppPool().addListener(this,
                new EventFilter(ConversationEvent.class));
    }

    /**
     * Start a conversation with the given contact
     * @param contact The contact to start a new conversation with.
     * @param initiatedByMe Whether the local user started the conversation
     * @return conversation The started conversation
     */
    public Conversation startConversation(Contact contact, boolean initiatedByMe) {
        if(!conversations.containsKey(contact)) {
            Conversation conversation = new Conversation(contact, initiatedByMe);
            conversations.put(contact, conversation);
            return conversation;
        } else {
            return conversations.get(contact);
        }
    }

    /**
     * Close the conversation with the given contact
     * @param contact The contact you want to stop chatting with
     */
    public void closeConversation(Contact contact) {
        conversations.remove(contact);
    }

    public void clear() {
        conversations.clear();
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
        // a conversation has been started
        if(event instanceof NewConversationEvent) {
            NewConversationEvent newConversationEvent = (NewConversationEvent)event;
            Contact contact = newConversationEvent.getContact();

            // verify that this request comes from a real contact
            if(contact != null && contact.isOnline()) {
                startConversation(newConversationEvent.getContact(),
                    !newConversationEvent.isExternal());
            }
        }

        // a conversation has been closed
        if(event instanceof CloseConversationEvent) {
            CloseConversationEvent closeConversationEvent = (CloseConversationEvent)event;
            if(!closeConversationEvent.isExternal()){
                closeConversation(closeConversationEvent.getContact());
            } else {
                // TODO what todo then?
            }
        }
    }
    
}
