package ch9k.configuration;

import org.jdom.Element;

/**
 * TODO
 * 
 * @author Bruno
 */
public class PersistentDataObject {

    private Element element;

    public PersistentDataObject(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public String getName(){
        return element.getName();
    }

}
