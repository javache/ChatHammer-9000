package ch9k.eventpool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;

/**
 * @author Pieter De Baets
 */
public class EventHeritageBuilder {
    private Multimap<Class<?>,String> analyzed = ArrayListMultimap.create();

    /**
     * Get a list of super-classes for a given event
     * @param event
     * @return expanded
     */
    public Collection<String> getIds(Class<? extends Event> event) {
        if(!analyzed.containsKey(event)) {
            analyze(event);
        }

        return analyzed.get(event);
    }

    private void analyze(Class<? extends Event> child) {
        Class<?> parent = child;
        while(!parent.equals(Object.class)) {
            analyzed.put(child, parent.getName());
            parent = parent.getSuperclass();
        }
    }
}
