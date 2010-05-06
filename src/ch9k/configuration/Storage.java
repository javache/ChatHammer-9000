package ch9k.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Used to manage persistable classes and store them on the users HD
 * @author Bruno
 */
public class Storage {
    /**
     * Hashmap containing the objects that we want to store
     */
    private HashMap<String, Persistable> storage;
    private HashMap<String, PersistentDataObject> xmlMap;

    /**
     * Username, used as filename to store files
     */
    private String username;

    /**
     * Creates and loads a new Storage class for the specified user.
     * Throws an exception if the user does not exist.
     * 
     * @param username Username we want to open
     */
    public Storage(String username){
        storage = new HashMap<String, Persistable>();
        xmlMap = new HashMap<String, PersistentDataObject>();
        this.username = username;

        // Check if the users exists, if not, don't even try to parse
        File file = new File(getStorageDirectory(), username.toLowerCase() + ".xml");
        if(!file.exists()) {
            return;
        }

        //Parse the XML file
        SAXBuilder parser = new SAXBuilder();
        try {
            Element root = parser.build(file).getRootElement();
            for (Object obj : root.getChildren()) {
                // Transform element into PersistentDataObject and put it in the XMLmap
                PersistentDataObject child = new PersistentDataObject((Element) obj);
                xmlMap.put(child.getName(), child);
            }
        } catch (IOException ex) {
            //File did not open
            System.err.println(ex);
        } catch (JDOMException ex) {
            //File did not parse
            System.err.println(ex);
        }

    }

    /**
     * Saves all the Persistables in their current state to the user's HD
     */
    public void save() {
        Logger.getLogger(getClass()).info("Saving configuration file");

        // Initiate the XML
        Element root = new Element("configuration");
        root.setAttribute("last-updated", new Date().toString());

        // Iterate through the persistables, and add them to the XML tree.
        for (Entry<String,Persistable> entry : storage.entrySet()) {
            Element child = entry.getValue().persist().getElement();
            //To be sure, check if the element has the right ID
            if (!child.getName().equals(entry.getKey())) {
                child.setName(entry.getKey());
            }
            child.setAttribute("class", entry.getValue().getClass().getName());
            root.addContent(child);
        }

        for (Entry<String,PersistentDataObject> pdo : xmlMap.entrySet()) {
            if(!storage.containsKey(pdo.getKey())){
                Element child = pdo.getValue().getElement();
                child.detach();
                root.addContent(child);
            }
        }
        
        try {
            // Open the right file
            File file = new File(getStorageDirectory(), username.toLowerCase() + ".xml");

            // Open the outpustream and write the XML
            FileOutputStream outputstream = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(root, outputstream);

            outputstream.close();
        } catch (IOException ex) {
            System.err.println(ex);
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
     * Fetches a previously stored object
     *
     * @param id name by which the object was stored
     * @return the previously stored object, or null if ID is unknown
     */
    public PersistentDataObject fetch(String id) {
        if(storage.containsKey(id)){
            return storage.get(id).persist();
        } else if(xmlMap.containsKey(id)){
            return xmlMap.get(id);
        } else {
            return null;
        }
    }

    /**
     * Returns the working directory for our application, OS independent
     *
     * @return File representing the directory in which our app stores it's userfiles.
     */
    public static File getStorageDirectory(){
        String userHome = System.getProperty("user.home", ".");
        String operatingSystem = System.getProperty("os.name").toLowerCase();

        File workingDirectory;
        if (operatingSystem.contains("windows")) {
            String applicationData = System.getenv("APPDATA");
            if (applicationData != null) {
                workingDirectory = new File(applicationData, "CH9K/");
            } else {
                workingDirectory = new File(userHome, ".CH9K/");
            }
        } else if (operatingSystem.contains("mac")) {
            workingDirectory = new File(userHome, "Library/Application Support/CH9K");
        } else {
            workingDirectory = new File(userHome, ".CH9K/");
        }

        if (!workingDirectory.exists()) {
            if (!workingDirectory.mkdirs()) {
                throw new RuntimeException("The working directory could not be created: " + workingDirectory);
            }
        }
        
        return workingDirectory;
    }
}
