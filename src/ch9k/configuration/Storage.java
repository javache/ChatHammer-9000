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
     * @throws IOException Specified user doesn't exist
     */
    public Storage(String username){
        storage = new HashMap<String, Persistable> ();
        xmlMap = new HashMap<String, PersistentDataObject> ();
        this.username = username;

        //Check if the users exists, if not, don't even try to parse
        if(!fileTest(username)) return;

        //Parse the XML file
        SAXBuilder parser = new SAXBuilder();
        File file = new File(getApplicationDirectory(), username.toLowerCase());
        try {
            Element root = parser.build(file).getRootElement();
            for (Object obj : root.getChildren()) {
                //Transform element into PersistentDataObject and put it in the XMLmap
                PersistentDataObject child = new PersistentDataObject((Element) obj);
                xmlMap.put(child.getName(), child);
            }
        } catch (IOException ex) {
            //File did not open
        } catch (JDOMException ex) {
            //File did not parse
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
            Element child = entry.getValue().persist().getElement();
            //To be sure, check if the element has the right ID
            if (!child.getName().equals(entry.getKey())) {
                child.setName(entry.getKey());
            }
            child.setAttribute("class", entry.getValue().getClass().toString());
            xml.addContent(child);
        }
        //Now store it somewhere on the HD
        try {
            //Open the right file
            File file =
                    new File(getApplicationDirectory(), username.toLowerCase());
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
    public static File getApplicationDirectory(){
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

    /**
     * Checks if we have config files for this use
     * @param username Username for whom we want to check files
     * @return Exists boolean
     */
    public boolean fileTest(String username){
        return new File(getApplicationDirectory(),
                username.toLowerCase()).exists();
    }
}
