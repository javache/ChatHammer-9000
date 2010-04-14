package ch9k.chat;

import ch9k.chat.events.CloseConversationEvent;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation extends AbstractListModel implements EventListener {
    private Contact contact;
    private boolean initiatedByMe;
    private Date startTime = new Date();
    private ConversationSubject subject;
    private List<ChatMessage> conversation = new ArrayList<ChatMessage>();

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     */
    public Conversation(Contact contact, boolean initiatedByMe) {
        this.contact = contact;
        this.initiatedByMe = initiatedByMe;
        
        EventPool.getAppPool().addListener(this, new ConversationEventFilter(this));
    }

    /**
     * Check whether or not this conversation is started by the current user.
     * @return initiatedByMe
     */
    public boolean initatedByMe() {
        return initiatedByMe;
    }

    /**
     * Get the chatting contact on the other end of the line.
     * @return contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Get the current subject of this conversation.
     * @return subject
     */
    public ConversationSubject getSubject() {
        return subject;
    }

    /**
     * Set the subject for this conversation.
     * @param subject
     */
    public void setSubject(ConversationSubject subject) {
        this.subject = subject;
    }

    /**
     * Get the date/time on witch this conversation was started
     * @return starttime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Adds a message to this Conversation.
     * @param chatMessage
     */
    public void addMessage(ChatMessage chatMessage) {
        int size = conversation.size();
        if(size == 0 || !chatMessage.equals(conversation.get(size-1))){
            conversation.add(chatMessage);
            fireIntervalAdded(contact, size, size);
        }
    }

    /**
     * Get the n last messages as Strings.
     * Most recent message will be last in line.
     * When there arent n messages the size of the returned array will be reduced to the number of messages.
     * @param n The number of messages to return
     * @return String[]
     */
    public String[] getMessages(int n) {
        if(n > conversation.size()) {
            n = conversation.size();
        }
        String[] messages = new String[n];
        int size = conversation.size();
        for(int i = size - n; i < size; i++){
            messages[i - size + n] = conversation.get(i).getText();
        }
        return messages;
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO delete this as listener
        EventPool.getAppPool().raiseEvent(new CloseConversationEvent(contact));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Conversation other = (Conversation) obj;
        if (this.contact != other.contact && (this.contact == null || !this.contact.equals(other.contact))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.contact != null ? this.contact.hashCode() : 0);
        return hash;
    }

    @Override
    public void handleEvent(Event event) {
        if(event instanceof NewChatMessageEvent){
            NewChatMessageEvent newChatMessageEvent = (NewChatMessageEvent) event;
            conversation.add(newChatMessageEvent.getChatMessage());
        }
    }

    @Override
    public int getSize() {
        return conversation.size();
    }

    @Override
    public Object getElementAt(int index) {
        return conversation.get(index);
    }
    
}
