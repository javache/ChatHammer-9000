package ch9k.configuration;

/**
 * Abstract representation of a persistable object.
 * @author Bruno
 */
public interface Persistable {
    /**
     * Store the object in its current state.
     * @return some persistent data.
     */
    PersistentDataObject persist();

    /**
     * Load object from a previously stored state.
     * @param object the object in which the data is stored.
     */
    void load(PersistentDataObject object);

}
