package ch9k.core.event;

import ch9k.core.ChatApplication;
import ch9k.core.ChatApplication;
import ch9k.eventpool.Event;

/**
 * @author Pieter De Baets
 */
public abstract class AccountEvent extends Event {
    @Override
    public Object getSource() {
        return ChatApplication.getInstance().getAccount();
    }
}
