package ch9k.chat;

public class ContactStatus {
    
    public enum Status {
        ONLINE,
        OFFLINE,
        REQUESTED,
        IGNORED,
        BLOCKED;
    }
    
    private Status status;
    
    private String text;
    
    public ContactStatus() {
        status = Status.OFFLINE;
        text = "";
    }
    
    public ContactStatus(Status status,String text) {
        this.status = status;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBlocked() {
        return status == Status.BLOCKED;
    }

    public boolean isRequested() {
        return status == Status.REQUESTED;
    }

    public boolean isOnline() {
        return status == Status.ONLINE;
    }
    
    public boolean isOffline() {
        return status == Status.OFFLINE;
    }
    
    public boolean isIgnored() {
        return status == Status.IGNORED;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}