package ch9k.chat;

import ch9k.chat.events.CloseConversationEvent;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation implements EventListener {
    private Contact contact;
    private boolean initiated;
    private Date starttime;
    private ConversationSubject subject;
    private Set<ChatMessage> conversation;

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     * @param activePlugins The list of the current active plugins on the initiators side.
     */
    public Conversation(Contact contact, boolean initiatedByMe) {
        this.starttime = new Date();
        this.conversation = new TreeSet<ChatMessage>();
        this.contact = contact;
        this.initiated = initiatedByMe;
        EventPool.getAppPool().addListener(this, new ConversationEventFilter(this));
    }

    /**
     * Check whether or not this conversation is started by the current user.
     * @return initiatad
     */
    public boolean initatedByMe() {
        return initiated;
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
        return starttime;
    }

    /**
     * Adds a message to this Conversation.
     * @param chatMessage
     */
    public void addMessage(ChatMessage chatMessage) {
        conversation.add(chatMessage);
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
        Iterator<ChatMessage> it = ((TreeSet<ChatMessage>)conversation).descendingIterator();
        int i = 0;
        while(n-- > 0) {
            messages[n] = it.next().getText();
        }
        return messages;
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO delete this as listener
        EventPool.getAppPool().raiseEvent(new CloseConversationEvent(this));
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
    
}
