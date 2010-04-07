package ch9k.chat;

import java.util.Date;

/**
 * Wrapper around a String that represents a chatMessage
 * @author Jens Panneel
 */
public class ChatMessage implements Comparable<ChatMessage>{
    private String text;
    private String writer;
    private Date time;

    /**
     * Constructor.
     * @param writer The username of the person who typed this ChatMessage.
     * @param text The actual text.
     */
    public ChatMessage(String writer, String text) {
        this.time = new Date();
        this.text = text;
        this.writer = writer;
    }

    /**
     * Get the actual text from this ChatMessage
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the name of the user that wrote this ChatMessage
     * @return writer
     */
    public String getWriter() {
        return writer;
    }

    /**
     * Get the time of this
     * @return ip
     */
    public Date getTime() {
        return time;
    }

    @Override
    /**
     * Compares To ChatMessages ordered by time
     * @param chatMessage The ChatMessage to compare with this one.
     */
    public int compareTo(ChatMessage chatMessage) {
        if(this.equals(chatMessage)) {
            return 0;
        }

        int compareTime = this.getTime().compareTo(chatMessage.getTime());
        if(compareTime == 0) {
            compareTime++;
        }
        return compareTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChatMessage other = (ChatMessage) obj;
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        if ((this.writer == null) ? (other.writer != null) : !this.writer.equals(other.writer)) {
            return false;
        }
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.text != null ? this.text.hashCode() : 0);
        hash = 53 * hash + (this.writer != null ? this.writer.hashCode() : 0);
        hash = 53 * hash + (this.time != null ? this.time.hashCode() : 0);
        return hash;
    }

}
