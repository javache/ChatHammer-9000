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
    /**
     * Hashmap containing the objects that we want to store
     */
    private HashMap<String, Persistable> storage;

    /**
     * Username, used as filename to store files
     */
    private String username;

    /**
     * Creates and loads a new Storage class for the specified user.
     * Throws an exception if the user does not exist.
     * 
     * @param username Username we want to open
     * @throws IOException Specified user doesn't exist
     */
    public Storage(String username) throws IOException {
        storage = new HashMap<String, Persistable> ();
        this.username = username;

        SAXBuilder parser = new SAXBuilder();
        File file = new File(getFilePath(), username.toLowerCase());
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
            //Open the right file
            File file = new File(getFilePath(), username.toLowerCase());
            //Open the outpustream and write the XML
            FileOutputStream outputstream = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(xml, outputstream);
            //Close
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

    private File getFilePath(){
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;
        String sysName = System.getProperty("os.name").toLowerCase();
        if (sysName.contains("windows")) {
            String applicationData = System.getenv("APPDATA");
            if (applicationData != null) {
                workingDirectory = new File(applicationData, ".CH9K/");
            } else {
                workingDirectory = new File(userHome, ".CH9K/");
            }
        } else if (sysName.contains("mac")) {
            workingDirectory = new File(userHome, "Library/Application Support/CH9K");
        } else if (sysName.contains("linux")) {
            workingDirectory = new File(userHome, ".CH9K/");
        } else {
            return new File(".");
        }

        if (!workingDirectory.exists())
            if (!workingDirectory.mkdirs())
                throw new RuntimeException("The working directory could not be created: " + workingDirectory);

        return workingDirectory;
    }
}
