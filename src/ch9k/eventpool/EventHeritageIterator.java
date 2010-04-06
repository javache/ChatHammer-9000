package ch9k.eventpool;

import java.util.Iterator;

/**
 * Iterates through the super-classes of a given Event class so Events
 * can be dispatched to listeners of superclasses as well
 * @author Pieter De Baets
 */
public class EventHeritageIterator implements Iterator<String> {
    private Class<?> baseClass;

    public EventHeritageIterator(Class<? extends Event> eventClass) {
        this.baseClass = eventClass;
    }

    @Override
    public boolean hasNext() {
        return !baseClass.equals(Object.class);
    }

    @Override
    public String next() {
        String className = baseClass.getName();
        baseClass = baseClass.getSuperclass();
        return className;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
