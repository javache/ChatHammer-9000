package ch9k.configuration;

import java.util.HashMap;

/**
 * Used to manage persistable classes and store them on the users HD
 *
 * @author Bruno
 */
public class Storage {

    private HashMap<String, Persistable> storage;

    /**
     * Saves some stuff I suppose?
     */
    public void save(){

    }

    /**
     * Add a persistable object to be stored
     *
     * @param id Id/Name for the object
     * @param obj Object to be stored.
     */
    public void store(String id, Persistable obj){
        storage.put(id,obj);
    }
    /**
     * Fetches a previously stored object?
     *
     * @param id name by which the object was stored
     * @return the previously stored object
     */
    public Persistable fetch(String id){
        return storage.get(id);
    }

}
