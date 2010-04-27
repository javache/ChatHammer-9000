package ch9k.chat;

public class ContactStatus {
    
    public enum Status {
        ONLINE,
        OFFLINE,
        REQUESTED,
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
    
    public Status getStatus() {
        return status;
    }
    
}