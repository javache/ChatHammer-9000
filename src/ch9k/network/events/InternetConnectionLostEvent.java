package ch9k.network.events;

import ch9k.eventpool.Event;

public class InternetConnectionLostEvent extends Event {
    // nobody should care where this event comes from
    public Object getSource() { return null; }
}