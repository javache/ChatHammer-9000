package ch9k.core.event;

/**
 * Broadcasted when an account changes it's status
 * @author Pieter De Baets
 */
public class AccountStatusEvent extends AccountEvent {
    private String status;

    public AccountStatusEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
