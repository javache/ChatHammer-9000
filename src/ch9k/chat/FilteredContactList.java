package ch9k.chat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author toon
 */
public class FilteredContactList extends AbstractListModel implements ListDataListener {
    /*
     * Our callback object
     */
    private ContactList contactList;

    /*
     * The filter used
     */
    private Set<ContactFilter> filterSet;

    /*
     * Represents a mapping from this array to the ContactList
     */
    private ArrayList<Contact> mapping;

    public FilteredContactList(ContactList contactList) {
        this.contactList = contactList;
        contactList.addListDataListener(this);

        mapping = new ArrayList<Contact>();
        filterSet = new HashSet<ContactFilter>();
        updateMapping();
    }

    public void addFilter(ContactFilter filter) {
        if(filterSet.add(filter)) {
            updateMapping();
        }
    }

    public void removeFilter(ContactFilter filter) {
        if(filterSet.remove(filter)) {
            updateMapping();
        }
    }

    private synchronized void updateMapping() {
        mapping.clear();
        
        for(Contact contact : contactList.getContacts()) {
            Iterator<ContactFilter> it = filterSet.iterator();
            boolean shouldDisplay = true;
            while(it.hasNext() && shouldDisplay) {
                shouldDisplay &= it.next().shouldDisplay(contact);
            }

            if(shouldDisplay) {
                mapping.add(contact);
            }
        }
        
        fireListChanged();
    }

    private void fireListChanged() {
        fireContentsChanged(this, 0, mapping.size());
    }

    @Override
    public int getSize() {
        return mapping.size();
    }

    @Override
    public Object getElementAt(int index) {
        return mapping.get(index);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        contentsChanged(e);
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        contentsChanged(e);
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        updateMapping();
    }
}
