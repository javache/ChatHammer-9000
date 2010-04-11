package ch9k.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Used to manage persistable classes and store them on the user's HD
 *
 * @author Bruno
 */
public class Storage {
    private HashMap<String, Persistable> storage;

    /**
     * Creates and loads a new Storage class for the specified user.
     * Throws an exception if the user does not exist.
     * 
     * @param username Username we want to open
     * @throws IOException Specified user doesn't exist
     */
    public Storage(String username) throws IOException {

        SAXBuilder parser = new SAXBuilder();
        File file = new File("ERRORZ");
        try {
            Element root = parser.build(file).getRootElement();
            for (Object obj : root.getChildren()) {
                Element child = (Element) obj;
                //initialize it
                store(child.getName(), null);
            }
        } catch (JDOMException ex) {
            //Do something, file is probably broken
        }

    }

    /**
     * Saves all the Persistables in their current state to the user's HD
     */
    public void save() {
        //Initiate the XML
        Element xml = new Element("Root");
        xml.addContent("ChatHammer 9000 storage file, created on " + new Date());

        //Iterate through the persistables, and add them to the XML tree.
        for (Entry<String, Persistable> entry : storage.entrySet()) {
            PersistentDataObject child = entry.getValue().persist();
            //To be sure, check if the element has the right ID
            if (!child.getName().equals(entry.getKey())) {
                child.setName(entry.getKey());
            }
            xml.addContent(child);
        }
        //Now store it somewhere on the HD
        try {
            File file = new File("ERRORZ");
            FileOutputStream outputstream = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(xml, outputstream);
            outputstream.close();
        } catch (IOException ex) {
            //something went wrong, todo
        }
    }

    /**
     * Add a persistable object to be stored
     *
     * @param id Id/Name for the object
     * @param obj Object to be stored.
     */
    public void store(String id, Persistable obj) {
        storage.put(id, obj);
    }

    /**
     * Fetches a previously stored object?
     *
     * @param id name by which the object was stored
     * @return the previously stored object
     */
    public Persistable fetch(String id) {
        return storage.get(id);
    }
}
