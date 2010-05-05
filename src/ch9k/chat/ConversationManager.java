package ch9k.chat;

import ch9k.chat.event.CloseConversationEvent;
import ch9k.chat.event.ContactOfflineEvent;
import ch9k.chat.event.ConversationEvent;
import ch9k.chat.event.NewChatMessageEvent;
import ch9k.chat.event.NewConversationEvent;
import ch9k.core.I18n;
import ch9k.core.event.AccountLogoffEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Manages the active conversations
 * @author Jens Panneel
 */
public class ConversationManager implements EventListener, Iterable<Conversation> {
    private Map<Contact, Conversation> conversations;

    public ConversationManager() {
        conversations = new HashMap<Contact, Conversation>();
        EventPool.getAppPool().addListener(this,
                new EventFilter(ConversationEvent.class));

        EventPool.getAppPool().addListener(new EventListener() {
            public void handleEvent(Event event) {
                clear();
            }
        }, new EventFilter(AccountLogoffEvent.class));

        EventPool.getAppPool().addListener(new ContactOfflineListener(),
                new EventFilter(ContactOfflineEvent.class));

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
     * @param conversation The conversation to close
     */
    public void closeConversation(Conversation conversation) {
        Logger.getLogger(ConversationManager.class).info(
                "Closing conversation with " + 
                conversation.getContact().getUsername());

        // send a system event
        NewChatMessageEvent systemEvent = new NewChatMessageEvent(
                conversation, new ChatMessage("info", I18n.get("ch9k.chat",
                    "contact_closed_conversation")), true);
        EventPool.getAppPool().raiseEvent(systemEvent);
    }

    public void clear() {
        Iterator<Conversation> it = conversations.values().iterator();
        while(it.hasNext()) {
            Conversation conversation = it.next();
            conversation.close();
            it.remove();
        }
    }

    /**
     * Get the conversation you have with the given contact
     * @param contact
     * @return conversation
     */
    public Conversation getConversation(Contact contact) {
        return conversations.get(contact);
    }

    private class ContactOfflineListener implements EventListener {

        @Override
        public void handleEvent(Event ev) {
            ContactOfflineEvent event = (ContactOfflineEvent)ev;
            Conversation conversation = conversations.get(event.getContact());
            if(conversation != null) {
                CloseConversationEvent closeEvent = new CloseConversationEvent(conversation);
                try {
                    closeEvent.setSource(InetAddress.getLocalHost());
                } catch(UnknownHostException ex) {
                }
                EventPool.getAppPool().raiseEvent(closeEvent);
            }
        }
        
    }

    private class AccountLogoffListener implements EventListener {
        @Override
        public void handleEvent(Event ev) {
            clear();
        }
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
            Conversation conversation = closeConversationEvent.getConversation();

            closeConversation(conversation);
            if(!closeConversationEvent.isExternal()) {
                conversation.close();
                conversations.remove(conversation.getContact());
            }
        }
    }

    @Override
    public Iterator<Conversation> iterator() {
        return conversations.values().iterator();
    }
}
